files = Dir["#{__dir__}/../../target/*.jar"].find_all do |file|
  file !~ /source|javadoc/
end

if files.length == 0
    exit "You have no dirgra releases in target.  Please run `mvn package`"
elsif files.length != 1
  exit "You have multiple dirgra releases in target.  Please clean up your workspace"
end

require_relative files[0]
