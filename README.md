
# Sentiment Analysis Service

This project provides a sentiment analysis service using Clojure and Python. The service exposes a REST API to analyze the sentiment of text data.

## Prerequisites

•  <a href="https://www.bing.com/search?form=SKPBOT&q=Clojure">[**Clojure**](https://www.bing.com/search?form=SKPBOT&q=Clojure)</a>: Ensure you have Clojure installed.

•  <a href="https://www.bing.com/search?form=SKPBOT&q=Python">[**Python**](https://www.bing.com/search?form=SKPBOT&q=Python)</a>: Ensure you have Python and pip installed, since you are going to need pysentimiento.  

If you install conda, make sure to set the `LD_LIBRARY_PATH` as follows:   
`````` export LD_LIBRARY_PATH="$(python3-config --prefix)/lib" `````` 

It is convenient to use a tool to manage the environment on a per-project basis, like direnv. If you go down that route, you will find the .envrc file in the base folder of the project managing the environmental variables for you.

•  <a href="https://www.bing.com/search?form=SKPBOT&q=Dependencies">Dependencies</a>: The project uses the following libraries:

•  ring

•  reitit

•  muuntaja

•  libpython-clj2

•  pysentimiento

Setup
1. <a href="https://www.bing.com/search?form=SKPBOT&q=Clone%20the%20repository">Clone the repository</a>:

git clone <repository-url>
cd <repository-directory>

1. Set up the environment:
•  Install Python dependencies:
```
pip install pysentimiento
```

•  Or create a conda environment and install pysentimiento.

Running the Service

1. Build the uberjar:
```
clj -T:build uber
```

2. Run the server:
```
java -jar target/sentiment-analysis-1.2.10-standalone.jar
```

3. Server will run on port 3000:    

server running on port 3000         

API Endpoints   
Health Check   
•  Endpoint: /health

•  Method: GET

•  Response:
```json
{
"status": 200
}
```

Sentiment Analysis
•  Endpoint: /sentiment

•  Method: POST

•  Request Body:
```json
{
"data": "Your text here"
}
```
•  Response:
```json
{
"sentimiento": "Positivo/Negativo/Neutro",
"probabilidades": {
"Positivo": "xx.x%",
"Negativo": "xx.x%",
"Neutro": "xx.x%"
}
}
```

Code Overview     
•  Namespace: sentiment-analysis.core         

•  Dependencies: Required libraries are imported at the beginning.a  

•  Python Initialization: Python environment is initialized using libpython-clj2.  
                                                                                    
•  Sentiment Analyzer: Created using pysentimiento.                                 
                                                                                    
•  Routes: Defined for health check and sentiment analysis.                         

•  Server: Configured to run on port 3000.                                          

License                                                                             
This project is licensed under the MIT License.                                     
