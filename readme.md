## city-distance-service

### Config
To run application just execute java -jar <jar_path>

The default port for application is 8080

### Main endpoints

    api/cities/links 
to create two cities if they're still not exist, and create link between them

    api/cities/paths
to find all paths between two cities

### Plugins
Checkstyle plugin is included in validate phase
PMD plugin (ruleset.xml) can be started by command: 

    mvn pmd:check
    
### Tests
All tests are included into test phase except integration and multithreading tests

