# inter-nodal
 
inter-nodal is the containerization of data with the purpose of isolating logic from data and the transport of data via containers, using multiple channels (http, tcp, mq, db, file, etc) and multiple formats (json, yaml, xml, html, jdbc, etc), without any handling of the data itself when changing modes. The method reduces data handling, and so improves security, reduces damage and loss, and allows data to be transported faster. The objective is to only interact with the fixed set of inter-nodal interfaces which consists of containers and meta-data, therefore the contents are irrelevant and would not be dependency or have any dependencies on any of the mechanisms surrounding or be effected by changes. Algorithms (e.g. business logic) can be written against the containers by referencing specific datapoints described in the meta-data.

meta-data modeling language example: https://github.com/bschorn/jane-bank/blob/master/src/main/resources/model/jane_bank.spec
meta-data definition file (created from modeling language): https://github.com/bschorn/jane-bank/blob/master/src/main/resources/meta/jane_bank.meta.json
