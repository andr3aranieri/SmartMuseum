# SmartMuseum

![alt tag](https://github.com/andr3aranieri/smartmuseumimages/blob/master/app_icon.png)

##Introduction

Smart Museum is a pervasive Android application that enhances user’s museum Experience. 
Using bluetooth low energy, the application is able to sense the physical environment and adapt its behavior detecting the exhibits around the user and estimating their distance from him.
Using a graph nosql database, the app stores every action of the user inside the museum, to be able to create a complete user history of visits.
The user will be able to 
- receive detailed informations about the exhibits around him directly on his smartphone;
- access his history of visits whenever, wherever you want; 
- interact with museum experts to clarify every doubt or to obtain more detailed informations about the exhibits;

The interaction with the physical environment is implemented using eddystone beacons:
- Monitoring: when the user approaches one of the museum entrances, he will receive a notification that invites him to start the app and to enter the museum;
- Ranging: when the user is inside the museum, the app detectes the exhibits around him showing them ordered by distance in real time;

The ask to an expert function is implemented using the http://smartmuseumask.slack.com/ Slack team. 
The Slack integration is done using a BOT token; it is transparent to the user (the user doesn’t use his Slack account), and in the future could be replaced by a proprietary message server.

