def write_training_set(name, training_set)
  File.open("./datasets/#{name}.txt", "w") do |f|
    training_set.shuffle.each do |dataset|
      if dataset[:language]
        line = "#{dataset[:commit]} #{dataset[:language].to_s} #{dataset[:watchers]} #{dataset[:liked]}\n"
        f.write(line)
      end
    end
  end
end
