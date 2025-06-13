# image2structure

This software provides a RESTful service for extracting chemical structures from images. More specifically, it encompasses a pipeline for extracting chemical structures from images (via OSR, i.e. optical structure recognition) classifying and eventually correcting the results from them. The open access OpenChemLib library was used to eliminate erroneous chemical structures with wrong isotopes, hypervalent atoms or other problems. The output is given as a JSON formatted response using the raw data as well as chemical compounds and Markush type scaffolds using SMILES format chemical data. Compounds and scaffolds with a molweight greater than 2000 are neglected. As identifiers both the OpenChemLib IDCode and the InChI is calculated for compounds. The IDCode is also calculated for Markush type scaffolds (smiles contain an *) to allow for substructure searching scaffolds.

image2structure is designed as a client-server solution. It can run either locally on a single machine or on a server that is accessible within a network. The OSR component is based on OSRA, a program for extracting chemical structures and reactions developed by Igor Filippov.

# How to build
First, install OSRA for all users - https://github.com/metamolecular/osra/blob/master/README. 
Than, `mvn package assembly:single` creates `target/i2s-distribution.tar.gz` archive which contains the application, resources, configurations and shell-scripts to run the application.

# How to use
Extract `i2s-distribution.tar.gz`. 
- `img2struct/bin` contains executables to run:
	`startImage2Struct.sh` starts application as a server
	`stopImage2Struct.sh` stops running server
	`i2sWithCurl.sh` executes requests on running server
	`i2s.sh` run the application to execute single action
- `img2struct/config` contains the configuration of the application

After service starts up, the following folders appear:
- `img2struct/bin` service logs
- `img2struct/run` service PID used to stop the service

# HTTP request
The request URL path is /img2struct thus a complete request URL would be
`http://SERVER:PORT/img2struct/performOSR/REQUEST` with SERVER set to the server address where the service runs and listens at given PORT (set to 9141 by default), and REQUEST set one of the possible requests below. The JSON formatted request data has to be provided in the content of the POST request:

`{"imageFilename":"/filelocation/test.png"}"}`

If processing was successful the response given by the service is a JSON-formatted string in the following form:

`{ "osrResult" : OUTCOME, "imageFilename" : "FILENAME" }`

As an example, test3.png which can be found in the test/resources folder shall return:

`{ "osrResult": { "compounds": [ { "smiles": "c1cc(Br)ccc1", "confidence": 1.0806, "resolution": 300, "page": 1, "position": "80x324-308x499", "inchi": "InChI=1S/C6H5Br/c7-6-4-2-1-3-5-6/h1-5H", "idcode": "gOpHAbILkW@@@@" } ], "markush": [ { "smiles": "C1CCCC*1", "confidence": 0.552267, "resolution": 300, "page": 1, "position": "684x320-827x494", "idcode": "gFpHJ@IRimUP@" } ], "rawData": "c1cc(Br)ccc1 79.7517 300 1.0806 1 80x324-308x499\nC1CCCC*1 79.3608 300 0.552267 1 684x320-827x494\nC1CC2C3CCC4C1C3C24 81.0853 300 0.40195 1 63x40-387x123" }, "fileName": "/Users/lweber/Desktop/ocrtest/test3.png" }`

The idcode is a unique identifier for small molecules, different to the InChI, it allows to reproduce the original structure.

If an error occurred, response takes the following form:

`{ "processState" : { "errorMsg" : "ERROR_MESSAGE", "errorCode" : ERROR_CODE, "processed" : false } }`

If the request was processed success fully it returns with status code 200. In case the request data could not be read, e.g. encoded content could not be decoded the status code will be 400. For other processing errors it returns with code 500.

# Notes
For debug purpose the server is configured by default to use fake OSRA executable (`img2struct/bin/osra-fake.sh`).

# Settings
Default settings can be modified using the config/i2s.properties file. For example:

`#port=9080`

`#maxThreads=100`

`#com.molgenie.img2struct.osr.OSRController.osrProgExecPath=bin`

`#osraExecutable=osra-fake.sh`

`osraExecutable=osra`

`chemFormat=smi`

`osrConfidence=0.0`

`osrTimeout=600`

`com.molgenie.img2struct.osr.OSRController.osrProgExecPath=/usr/local/bin`


osrConfidence can be set to return only such compounds that have a higher confidence value, e.g. a confidence of 1.0 is recommended.

osrTimeout is a time out for OSR processing in seconds, please note that processing PDFs takes longer than clipped images.

chemFormat may be smi, sdf, rxn, rsmi. Currently only in case of smi the chemistry corrections are used.

