Assumptions:

1. From the delorean.doc examples of interaction, there is an example of 'GET 0 110' which in the example returned 
'OK 1.6'. Since the latest data updated was 'UPDATE 0 105 1.6' with a different timestamp, if the command is of non 
existent timestamp it would attempt to return a latest record. 
Also this statement: 'The observation for an identifier "as-of" a timestamp is found by searching in the identifier's 
history for the observation with the greatest timestamp that is less than, or equal to, the sought timestamp' to support
that assumption.

2. On each field used an Object type such as, Integer.class for id. That's in order to avoid having a default value which
is 0 for the type int.

3. When DELETE operation has a timestamp, given this statement 'If timestamp is provided, deletes all observations for the 
given identifier from that timestamp forward' assumption is - to delete all observations greatest or equal to the 
timestamp.

4. When Updating 'Returns the data from the prior observation as-of that timestamp' 

5. Fields validation according to
    Items in <angle brackets> represent required arguments, but the angle brackets themselves do not form part of the command
    Items in [square brackets] are optional arguments


Code was written on the IntelliJ IDE
Maven is used as a build tool