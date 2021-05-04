# Customer Management Proof of Concept

## Background

This repository provides two proof of concept applications that sort customer data. One is a command line application that reads a delimited file and outputs sorted delimited data. The other application is a RESTful server. Data can be uploaded to the server with a POST endpoint. Sorted data can be returned with three different GET endpoints. Both applications provide three sorting options:

- sorted by favorite color then by last name ascending
- sorted by birth date, ascending
- sorted by last name, ascending

## Dependencies

Building and running these applications requies Java 1.8 or later and the [Scala build tool](https://www.scala-sbt.org/).

## Building the Applications

To build the applications, run the `buildApps.sh` script in the root directory of the repository. The script builds tarballs for both the command line application and the RESTful application. The script requires a target directory argument specifying where the tarballs should be placed. Example:

```
 ~  customer-management-poc   main  %   ./buildApps.sh ~
[info] welcome to sbt 1.5.0 (Private Build Java 14.0.2)
[info] loading settings for project customer-management-poc-build from plugins.sbt ...
[info] loading project definition from /home/cdavis/customer-management-poc/project
[info] loading settings for project aggregate from build.sbt ...
[info] set current project to aggregate (in build file:/home/cdavis/customer-management-poc/)
[warn] sbt 0.13 shell syntax is deprecated; use slash syntax instead: recordSorterCmd / Universal / packageZipTarball, customerRestManagement / Universal / packageZipTarball
[info] Wrote /home/cdavis/customer-management-poc/customer-rest-management/target/scala-2.12/customer-rest-management_2.12-0.1.0-SNAPSHOT.pom
[info] Wrote /home/cdavis/customer-management-poc/record-sorter-cmd/target/scala-2.12/record-sorter-cmd_2.12-0.1.0-SNAPSHOT.pom
[info] Wrote /home/cdavis/customer-management-poc/core/target/scala-2.12/core_2.12-0.1.0-SNAPSHOT.pom
.
.
.
```

## Running the command line application

First, untar the `record-sorter-cmd` tarball created in the previous section:

```
 ~  %  tar xzvf record-sorter-cmd-0.1.0-SNAPSHOT.tgz
record-sorter-cmd-0.1.0-SNAPSHOT/
record-sorter-cmd-0.1.0-SNAPSHOT/lib/
record-sorter-cmd-0.1.0-SNAPSHOT/lib/org.daviscale.record-sorter-cmd-0.1.0-SNAPSHOT.jar
record-sorter-cmd-0.1.0-SNAPSHOT/lib/org.scala-lang.scala-library-2.12.13.jar
record-sorter-cmd-0.1.0-SNAPSHOT/lib/org.daviscale.core-0.1.0-SNAPSHOT.jar
record-sorter-cmd-0.1.0-SNAPSHOT/bin/
record-sorter-cmd-0.1.0-SNAPSHOT/bin/record-sorter-cmd.bat
record-sorter-cmd-0.1.0-SNAPSHOT/bin/record-sorter-cmd
```

Then, navigagte to the `bin` subdirectory of the expanded folder. The script is executed simply with `./record-sorter-cmd`. A help message is printed if there are no arguments.

```
 ~  record-sorter-cmd-0.1.0-SNAPSHOT  bin  %  ./record-sorter-cmd
Invalid usage.

Correct usage is as follows.
./record-sorter-cmd [filename] [sorting-method]

Filename must refer to a comma, pipe, and space delimited file
Sorting method must be one of: ColorAndLastName, BirthDate, LastName
Example usage:
./record-sorter-cmd myCustomers.csv BirthDate

```

Here's an example with a valid filename and sorting option:

```
 ~  record-sorter-cmd-0.1.0-SNAPSHOT  bin  %  ./record-sorter-cmd ~/customer-management-poc/core/src/main/resources/pipeDelimited.txt ColorAndLastName
Sorted by color and last name in ascending order:

Hill | Sally | volunteer@example.org | Blue | 06/28/1983
Smith | Jane | volunteer@example.org | Blue | 09/23/1995
Washington | Frank | noreply@example.com | Blue | 09/11/1952
Hill | Frank | noreply@example.com | Indigo | 03/19/1944
Miller | Frank | volunteer@example.org | Indigo | 07/17/1959
.
.
.
```

## Running the RESTful app

First, untar the tarball created in the "Building" section:

```
 ~  %  tar xzvf customer-rest-management-0.1.0-SNAPSHOT.tgz
customer-rest-management-0.1.0-SNAPSHOT/
customer-rest-management-0.1.0-SNAPSHOT/lib/
customer-rest-management-0.1.0-SNAPSHOT/lib/com.twitter.hpack-1.0.2.jar
customer-rest-management-0.1.0-SNAPSHOT/lib/com.typesafe.akka.akka-actor_2.12-2.6.14.jar
customer-rest-management-0.1.0-SNAPSHOT/lib/com.typesafe.akka.akka-parsing_2.12-10.2.4.jar
customer-rest-management-0.1.0-SNAPSHOT/lib/io.spray.spray-json_2.12-1.3.6.jar
customer-rest-management-0.1.0-SNAPSHOT/lib/com.typesafe.akka.akka-http-spray-json_2.12-10.2.4.jar
customer-rest-management-0.1.0-SNAPSHOT/lib/com.typesafe.akka.akka-protobuf-v3_2.12-2.6.14.jar
customer-rest-management-0.1.0-SNAPSHOT/lib/org.scala-lang.scala-library-2.12.13.jar
customer-rest-management-0.1.0-SNAPSHOT/lib/org.reactivestreams.reactive-streams-1.0.3.jar
customer-rest-management-0.1.0-SNAPSHOT/lib/com.typesafe.config-1.4.0.jar
customer-rest-management-0.1.0-SNAPSHOT/lib/org.daviscale.core-0.1.0-SNAPSHOT.jar
customer-rest-management-0.1.0-SNAPSHOT/lib/org.scala-lang.modules.scala-java8-compat_2.12-0.8.0.jar
customer-rest-management-0.1.0-SNAPSHOT/lib/org.daviscale.customer-rest-management-0.1.0-SNAPSHOT.jar
customer-rest-management-0.1.0-SNAPSHOT/lib/com.typesafe.akka.akka-http_2.12-10.2.4.jar
customer-rest-management-0.1.0-SNAPSHOT/lib/com.typesafe.ssl-config-core_2.12-0.4.2.jar
customer-rest-management-0.1.0-SNAPSHOT/lib/com.typesafe.akka.akka-http-core_2.12-10.2.4.jar
customer-rest-management-0.1.0-SNAPSHOT/lib/org.scala-lang.modules.scala-parser-combinators_2.12-1.1.2.jar
customer-rest-management-0.1.0-SNAPSHOT/lib/com.typesafe.akka.akka-stream_2.12-2.6.14.jar
customer-rest-management-0.1.0-SNAPSHOT/bin/
customer-rest-management-0.1.0-SNAPSHOT/bin/customer-rest-management.bat
customer-rest-management-0.1.0-SNAPSHOT/bin/customer-rest-management
```

Then, navigate to the `bin` directory and start the app:

```
 ~  customer-rest-management-0.1.0-SNAPSHOT  bin  %  ./customer-rest-management
Server online at http://localhost:8080/
```

Data can be added to the application with the `addCustomers.sh` script:

```
./addCustomers.sh core/src/main/resources/commaDelimited.txt
{
  "message": "Record added"
}{
  "message": "Record added"
}{
  "message": "Record added"
}{
  "message": "Record added"
}
.
.
.
```

That script uses the application's POST endpoint to upload data. Then, you can use the GET endpoints to view sorted data.

```
curl http://localhost:8080/records/color

{
  "customerRecords": [{
    "dateOfBirth": "07/26/1975",
    "email": "volunteer@example.org",
    "favoriteColor": "Blue",
    "firstName": "Mary",
    "lastName": "Hill"
  }, {
    "dateOfBirth": "07/13/1996",
    "email": "business@example.com",
    "favoriteColor": "Blue",
    "firstName": "Jane",
    "lastName": "Jones"
  }, {
.
.
.
```

```
curl http://localhost:8080/records/birthdate

{
  "customerRecords": [{
    "dateOfBirth": "07/16/1947",
    "email": "noreply@example.com",
    "favoriteColor": "Blue",
    "firstName": "Frank",
    "lastName": "Washington"
  }, {
    "dateOfBirth": "08/11/1952",
    "email": "noreply@example.com",
    "favoriteColor": "Yellow",
    "firstName": "Mary",
    "lastName": "Miller"
  }, {
.
.
.
```

```
curl http://localhost:8080/records/name

{
  "customerRecords": [{
    "dateOfBirth": "02/26/1989",
    "email": "volunteer@example.org",
    "favoriteColor": "Red",
    "firstName": "Bob",
    "lastName": "Washington"
  }, {
    "dateOfBirth": "12/13/1968",
    "email": "noreply@example.com",
    "favoriteColor": "Yellow",
    "firstName": "Sally",
    "lastName": "Washington"
  }, {
.
.
.
```
