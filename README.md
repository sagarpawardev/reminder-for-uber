# reminder-for-uber

This application calculates the estimated time to book a cab for reaching a particular destination.

Tech-stack:
- JSP
- Spring
- Java
- Retrofit2

Pre Requisites:
- Java 1.8
- Maven
- Heroku CLI

## Steps to deploy on local Tomcat:
1. Go to project root directory and Clean Project using command `mvn clean`
2. Run tomcat server using command `mvn tomcat7:run`

## Steps to deploy on Heroku:
1. Create war file using command `mvn package`
2. Open terminal and type `heroku login`
3. Once logged in, use command `heroku war:deploy timetobookuber.war --app <heroku_app_name>` to deploy

## Basic Alogrithm:

    trip_start_time = findStartTime()
    if( trip_start_time < current_time)
        throw Exception

     check(trip_start_time)

    function check( trip_start_time, decayed_cab_arrival_time ):
        cab_arrival_time = findCabArrivalTime()
        cab_arrival_time = time_decay_algo(cab)
        book_time = start_time - cab_arrival_time

        time_left = book_time - current_time
        if time_left > 60:
            schedule( check(trip_start_time) , book_time-60)
        else if time_left > 1:
            schedule( check(trip_start_time), book_time - time_left/2)
        else:
            send_mail()
    
# Live Demo
You can check live demo [here](https://time-to-book-uber.herokuapp.com/)
