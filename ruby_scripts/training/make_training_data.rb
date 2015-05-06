require 'octokit'
require_relative 'writer'
require_relative '../config'

training_set_count = 0
loop do
  puts "What is the username?"
  github_username = gets.chomp
  break if github_username.eql?("-")
  @client.starred(github_username)
  training_data_set = []

  ## get the data for the user from their stars
  puts "Compiling training data from #{github_username}"
  repository_response = @client.last_response
  loop do
    begin
      repository_response.data.each do |repo|
        training_data_set << compile_training_data(repository_response.data.delete(repo), 1)
      end

      if repository_response.rels[:next].eql?(nil)
        break
      else
        puts "getting the next batch"
        repository_response = repository_response.rels[:next].get
      end
    rescue Octokit::NotFound, Octokit::RepositoryUnavailable => e
      puts e
    end
  end

  ## get random repositories that they haven't starred
  new_repo_count = 0
  rescued_error_count = 0
  not_added_repo_count = 0

  @client.all_repositories
  repository_response = @client.last_response
  loop do
    begin
      repo_to_add = repository_response.data.sample
      previous_count = new_repo_count
      repo_data = compile_training_data(repo_to_add, 1)

      if !training_data_set.include?(repo_data)
        repo_data[:liked] = 0
        training_data_set << repo_data
        new_repo_count += 1
      end

      if previous_count.eql?(new_repo_count)
        not_added_repo_count += 1
      end

      if rescued_error_count.eql?(20) || not_added_repo_count.eql?(10)
        rand(30).times { repository_response = repository_response.rels[:next].get }
        rescued_error_count = 0
        not_added_repo_count = 0
      end
    rescue Octokit::NotFound, Octokit::RepositoryUnavailable => e
      rescued_error_count += 1
      puts "Rescued a GitHub API error. README or a repository doesn't exist. Continuing on."
    ensure
      break if new_repo_count.eql?(NEW_REPO_TRAINING_DATA_COUNT)
    end
  end


  puts training_data_set.inspect
  write_training_set("training_data_set_#{training_set_count}", training_data_set)
  training_set_count += 1
end
