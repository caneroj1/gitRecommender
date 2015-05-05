require 'octokit'

require_relative '../config'

puts "What is the username?"
github_username = gets.chomp
@client.starred(github_username)
training_data_set = []

puts "Compiling training data from #{github_username}"
repository_response = @client.last_response
loop do
  begin
    repository_response.data.each do |repo|
      training_data_set << compile_training_data(repository_response.data.delete(repo))
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

puts training_data_set.inspect
