Kie ML Kie Server Extension
--

Allow models to run on Kie Server. The published endpoints are:

GET `server/containers/{id}/kieml`: Returns all the models available for container with {id}
GET `server/containers/{id}/kieml`/{modelId}`: Returns the information about the specific model
POST `server/containers/{id}/kieml`/{modelId}`: Run a prediction for model modelId. The payload should be a marshalled Input object.
