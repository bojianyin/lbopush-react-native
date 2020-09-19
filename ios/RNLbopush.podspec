require 'json'
pjson = JSON.parse(File.read('../package.json'))

Pod::Spec.new do |s|
  s.name         = "RNLbopush"
  s.version      = pjson["version"]
  s.summary      = "RNLbopush"
  s.description  = <<-DESC
                  RNLbopush
                   DESC
  s.homepage     = "https://github.com/bojianyin/lbopush-react-native"
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/author/RNLbopush.git", :tag => "master" }
  s.source_files  = "*.{h,m}"
  s.requires_arc = true


  s.dependency "React"
  #s.dependency "others"

end

  