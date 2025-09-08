Software Technology Experiment 2: REST API with Spring Boot

The beginning of the exercise was quite simple as building on the previous experiment, there was the task of
implementing the structure as a normal java code. However in this stage I made the mistake of not implementing IDs
for every object which made things very complicated and disorganised, later in the exercise I decided to implement it
though it required an overhaul of most of the methods.

Step 3 required creating test scenarios, which I simply decided to do using curl. The tests came in quite handy for the
next step, as I had a bit of problems with colliding mapping in my controllers. In the fourth step I also ran into my
missing ID problem, atleast ive learnt now to think through the entire structure during construction, and never skip
the IDs. The IDs combined with the JSON identity Reference and Info, layed out the resource identification.

Automating testing was ok, having logic already in order combined with intellij functionality, which makes it generate
almost by itself, though I could have saved a lot of time using the HTTP client instead of Java, as I was using curl
anyways.