# Heisenbug
Projects

Give a concise overview of your design. How did you divide the code into classes and methods? How does your design for Project 5 differ from the design you inherited for Project 4? How did you respond to feedback?

The gesture class reads an arraylist and uses a visual element for user interaction like a note bar; it inherits same codes as the notebar from a common superclass. We divided the code into the following classes: Midiplayer, Tunecomposer, Items, Notebar, Gesture and Playbar. Tunecomposer houses the methods used in composing; Items is a superclass that sets the basic attributes such of both notebars and gestures; the rest are taken in from older projects. The starting codes we used came from both Will's group and the example.
			 
Explain why your way was the elegant way to do it. Address any improvements you made based on my feedback.
Our gesture class did not inherit from the notebox class or vice versa; instead they both belong to a common superclass "Item". This is   because a gesture by definition has to be more like a container and a notebox doesn't have that property. If we tried to make gesture     inherit from notebox then stretching a gesture would stretch the black box around the gesture(or maybe do nothing) rather than stretching   the individual notes contained within it. Similarly, deleting the gesture wouldn't delete the individual notes within it.
