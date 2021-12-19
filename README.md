"# CovidPredictor" 
<h1>CovidPredictor</h1>
This project is about prediction of Covid-19 brakes, and notify 
hospitals about predicted outbrake for the next week.
This will alert the hospitals to be preperd to receive bigger 
number of patients and get more manpower.

The system build with 3 mircroservices, Gateway, msdb, analytics.

<h3>Gateway</h3>
The Gateway responsible to authorize users
by Spring Security to enter the system UI.

<h3>msdb</h3>
A system DB, responsible for retrieving patients data from MOH API
Contain notification thread to monitor outbreaks 
and send messages to the hospital in case of prediction of outbreaks.

Data is stored in by patients model in MySql DB.

<h3>Analytics</h3>
The Analytics gets data from msdb and produces a daily report.
Each report contains data about the patients hospitalized, including historical data.
When analyzing the information, can give a prediction of the estimated number
of patients that will be next week. 


<h2>Run</h2>

To run the application:
1. Open every service in its own IDE.
2. Install Maven dependecies.
3. Run by order: Gateway -> Analytics -> msdb 
4. Go to http://localhost:8080/
5. Register and sing in.

<h3>Register</h3>

![image](https://user-images.githubusercontent.com/45357452/146671394-d3b8db65-a58e-422a-9cd6-50f6db9181f2.png)

<h3>Sing in</h3>

![image](https://user-images.githubusercontent.com/45357452/146671417-4b921f56-3492-45a6-bddf-4587fc56be0d.png)

<h3>Main screen</h3>

![image](https://user-images.githubusercontent.com/45357452/146671434-2f314f98-69ec-4b12-8ef4-b10e850056a0.png)

<h3>Reports</h3>

![image](https://user-images.githubusercontent.com/45357452/146671479-d5258be4-e54e-433b-bf33-16c90722ed15.png)

