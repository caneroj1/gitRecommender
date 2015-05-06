## to create the database table
# CREATE TABLE repositories (
# id integer PRIMARY KEY,
# name text,
# readme_url text,
# pushed_at timestamp,
# watchers integer,
# languages hstore
# );

require 'active_record'
require 'pg'
require 'octokit'

require_relative '../config'

new_repo_count = 0
rescued_error_count = 0
not_added_repo_count = 0
@client.all_repositories
repository_response = @client.last_response

loop do
  begin
    repo_to_add = repository_response.data.sample
    previous_count = new_repo_count
    new_repo_count += insert_new_repo(@client, repo_to_add)

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
    break if new_repo_count.eql?(NEW_REPO_COUNT)
  end
end
