# GitHub Demo Application: 
Is an application for getting given github user repositories details: repository name, repository owner, repository branches with its sha.
The application builds with Spring Boot (contains run app method and properties file) and uses kohsuke library for getting connection with a given Github user account. 
Application body is basicly created with service and controller. GithubService contains two methods: first one connects to a GitHub account and creates a list of repositories 
that are not forks. Second method should create a Map of given user repositories together with its branches and commits sha. Connection is created anonymously,
with a built-in kohsuke library method. Github Controller presents result in json format, using @RestController annotation.
For obtaining result, it is necessary to introduce a correct URL pattern that contains requested Github user name.
