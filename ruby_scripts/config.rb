require "dotenv"
require 'active_record'
Dotenv.load

ActiveRecord::Base.establish_connection(
  adapter:  'postgresql',
  host:     ENV["host"],
  database: ENV["database_name"],

  username: ENV["database_username"]
)

class Repository < ActiveRecord::Base
end

@client = Octokit::Client.new(login: ENV["login"],
                              password: ENV["password"],
                              access_token: ENV["token"])
NEW_REPO_COUNT = 1000
NEW_REPO_TRAINING_DATA_COUNT = 20

def compile_training_data(response, liked)
  puts "Processing #{response[:full_name]}"
  data = { liked: liked }
  data[:id]       = response[:id]
  data[:watchers] = response[:watchers]
  data[:commit]   = get_last_commit_time(response[:full_name]).strftime('%s')
  data[:language] = get_languages(@client.languages(response[:full_name])).to_a.max { |a, b| a[1] <=> b[1] }
  data[:language] = data[:language][0] unless data[:language].nil?
  data
end

def get_number_of_watchers(api_response)
  return 0 if api_response.data.count.eql?(0)
  return api_response.data.count if api_response.rels.empty?
  number_of_pages = api_response.rels[:last].href.match(/page=(\d+)$/)[1].to_i
  count_on_last_page = api_response.rels[:last].get.data.count.to_i
  (count_on_last_page + (number_of_pages - 1) * 30)
end

def get_last_commit_time(repo_name, main_branch = nil)
  main_branch ||= "master"
  @client.branch(repo_name, main_branch)[:commit][:commit][:author][:date]
end

def get_languages(api_response)
  language_hash = {}

  api_response.each { |key, value| language_hash.store(key, value) }
  language_hash
end

def insert_new_repo(github_client, repo)
  full_name = repo.full_name

  readme_url = github_client.readme(full_name).download_url
  github_client.stargazers(full_name)
  watchers = get_number_of_watchers(github_client.last_response)
  languages = get_languages(github_client.languages(full_name))
  last_main_commit = get_last_commit_time(full_name, repo["default_branch"])

  if Repository.find_by_id(repo.id).nil?
    new_repo = Repository.new(id: repo.id,
                              name: full_name,
                              readme_url: readme_url,
                              pushed_at: last_main_commit,
                              watchers: watchers,
                              languages: languages)

    if new_repo.save
      puts "Successfully saved!"
      puts new_repo.inspect
      return 1
    else
      puts "Errors..."
      puts new_repo.errors
      return 0
    end
  else
    puts "Repository with id #{repo.id} and name #{full_name} already exists."
    return 0
  end
end
