# SpringBoot Todo List

## TaskApp 2023

This is a SpringBoot Todo List app that allows to perform CRUD operations on given items. The data are stored in PostgreSQL database using Hibernate and JpaRespository functionalities. Frontend was created using React and JavaScript.

## Run Locally

Clone the project

```bash
  git clone https://github.com/michal-bed/springboot_todolist.git
```

The app is pre-configured and ready to run. In case of any issues, you can contact with me by GitHub.


You will need to run React and SpringBoot apps independently. You can use built-in IDE configurations to achieve this. But it is also possible to run the app from command line. Here I will only describe how to install necessary dependencies from `package.json` and run the React app. For more info about running SpringBoot app from the IDE consult for instance [this article](https://www.geeksforgeeks.org/how-to-run-spring-boot-application/).


Open terminal from `todolist-client` directory and run
```bash
  npm install
```

It is recommended to run the React app on port 3000 (default) or if it is already occuppied, then on port 3001.

When the dependencies installation is finished, you can run the app typing in the console:
```bash
  npm start
```
Besides, you can also use a built-in IDE functionality to start the app. The SpringBoot app should be run on port 8080 as the REST API endpoints are set up for this port. If you want to run it on a different port, then you need to adjust the `REACT_APP_REST_API_PORT` value in `.env` file which is located in webapp `todolist-client` folder.


## Environment Variables

To run this project, you will need to set the following environment variables. The project is configured to utilize PostgreSQL database. All these values are specific to the database system in use.

`dbUser` - database user

`dbPassword` - database user password

`dbUrl` - database connection url



## Monitoring Endpoints

The application uses Spring Boot Actuator to monitor endpoints, among other things, it allows to trace the REST API requests and provides a detailed overview of perfomed actions and their results. You can find more info on the subject for example [here](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html).

Here you can see some example endpoints that provide more details about REST API requests:

`http://localhost:8080/actuator/metrics/http.server.requests?tag=uri:/api/todo-item` - information about requests for the endpoint at `/api/todo-item` uri accessible by the app, among other things, it provides information about the number of performed requests - look at `COUNT` value

`http://localhost:8080/actuator/metrics/http.server.requests?tag=uri:/api/todo-item&tag=method:GET` - information about the requests for the same endpoint but only using GET method

`http://localhost:8080/actuator/metrics/http.server.requests?tag=uri:/api/todo-item&tag=status:200` - requests with the response status of value '200'















 

