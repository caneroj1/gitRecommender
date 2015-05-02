require 'active_record'
require 'pg'
require 'octokit'

require_relative 'config'

new_repo_count = 0
rescued_error_count = 0
not_added_repo_count = 0

loop do
  puts "Enter GitHub username: "
  username = gets.chomp

  @client.starred(username)
  repository_response = @client.last_response

  loop do
    begin
      puts repository_response.rels.inspect
      repository_response.data.each do |repo|
        repository_response.data.delete(repo)
        new_repo_count += insert_new_repo(@client, repo)
      end
      if repository_response.rels[:next].eql?(nil)
        break
      else
        puts "getting the next"
        repository_response = repository_response.rels[:next].get
      end
    rescue Octokit::NotFound, Octokit::RepositoryUnavailable => e
      puts "Rescued a GitHub API error. README or a repository doesn't exist. Continuing on."
    end
  end
end

puts "Added #{new_repo_count} new repositories."
