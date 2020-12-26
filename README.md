# Introduction :
The aim of this project is to design and implement a simple mediator, using different sources
of data in the field of cinema. The mediator integrates 3 sources of data :
- A local relational database, which will be filled with data extracted from the web, using **Talend ETL** and other tools.
- The LOD source DBpedia (https://dbpedia.org), where you can consult information on  on cinema: films, actors, directors, producers, etc.
- The Open Movie Database source (http://www.omdbapi.com/), accessible through services such as **web REST APII**, which offers information about movies.


## Talend to fill the local database : 
Talend is a software package specialized in data integration. We use it to export the information contained in our csv files to our local database. There will be an SQL table within our database, which is the moviesBudgetsTable, on which we will "upload" the contents of the moviesBudgest.csv file as well as all the genre and distributor information present in the genre csvs we previously generated using jsoup.

<p align="center" width="100%">
    <img width="60%" src="https://github.com/AmineAgrane/data_integration_from_various_sources/blob/main/doc/talend..PNG"> 
</p>


## Dpedia Requests

DBpedia can be accessed through the access point http://dbpedia.org/sparql where you can send requests in **SPARQL** format. The DBpedia source is used to extract additional information about the movies.
You can see below an example of a **SPARQL** request addressed to dpedia :
~~~~sql
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#
PREFIX dbo: <http://dbpedia.org/ontology/>
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX : <http://dbpedia.org/resource/>

SELECT ?aname WHERE {
?film a dbo:Film ;
foaf:name "MOVIE_TITLE" @en ;
dbo:starring ?actor .
?actor foaf:name ?aname.
}
~~~~


## OMDB Requests 
OMDb API is a RESTful web service that allows you to get information about movies. This site will be used to obtain information about the summary (plot) of a movie.

The results of the OMDB API queries are returned as XML files, on which we will use XPath queries to extract the information we are interested in. OMDB Api being a REST web service, the requests to retrieve data are GET REQUESTS supported by the HTTP protocol. It remains then to define the url to use in order to perform our GET REQUEST.


<p align="center" width="100%">
    <img width="60%" src="https://github.com/AmineAgrane/data_integration_from_various_sources/blob/main/doc/omd.png"> 
</p>

# The Mediator
The mediator allows us to perform a virtual integration of our data, it takes the request addressed to the global schema that it breaks down into subqueries in order to pass it to the different sources. Then, it will take care of retrieving and merging the different answers in order to build a coherent and homogeneous final result. The architecture of our project is as follows:

<p align="center" width="100%">
    <img width="50%" src="https://github.com/AmineAgrane/data_integration_from_various_sources/blob/main/doc/mediator.png"> 
</p>

## Libs : 

This project uses a number of open source library to work properly:

* [apache-jena](https://mvnrepository.com/artifact/org.apache.jena/jena-core/3.14.0) Apache Jena is an open source Semantic Web framework for Java. It provides an API to extract data from and write to RDF graphs. The graphs are represented as an abstract "model". A model can be sourced with data from files, databases, URLs or a combination of these. A model can also be queried through SPARQL 1.1. Jena is similar to RDF4J (formerly OpenRDF Sesame); though, unlike RDF4J, Jena provides support for OWL (Web Ontology Language). The framework has various internal reasoners and the Pellet reasoner (an open source Java OWL-DL reasoner) can be set up to work in Jena.

* [mysql-connector-java](https://www.w3resource.com/mysql/mysql-java-connection.php) : MySQL provides connectivity for Java client applications with MySQL Connector/J, a driver that implements the Java Database Connectivity (JDBC) API. The API is the industry standard for database-independent connectivity between the Java programming language and a wide range of SQL databases.

* [jsoup](https://jsoup.org/download) : jsoup is a Java library for working with real-world HTML. It provides a very convenient API for fetching URLs and extracting and manipulating data, using the best of HTML5 DOM methods and CSS selectors. jsoup implements the WHATWG HTML5 specification, and parses HTML to the same DOM as modern browsers do.

* [opencsv](http://opencsv.sourceforge.net/) - Opencsv is an easy-to-use CSV (comma-separated values) parser library for Java. It was developed because all the CSV parsers at the time didn’t have commercial-friendly licenses. Java 8 is currently the minimum supported version.
