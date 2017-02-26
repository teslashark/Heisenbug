## Reflection
#### by Cooper, Emma, and Ben L.

>Give a concise overview of your design. How did you divide the code into classes and methods? How does your design for Project 3 differ from your design(s) for Project 2?

Anwser

>Explain why your way was the elegant way to do it. Address any improvements you made based on my feedback.

Answer

>Explain what, if anything, in your solution is inelegant and why you didn't make it elegant (for example, maybe you didn't have time or the knowledge to fix it).

I think we could have separated our solution into more classes in order to better separate the functions of each class into exactly what they do. For example, a possible "note" class may be helpful, especially if in the future we expand to have different types of notes (with different lengths, etc.) that would be helpful so that the tuneComposer class builds the pane and then uses the note class and playBar class in order to build a tune and then play it with MidiPlayer. We didn't do this immediately this time since this is more of an idea for the future as the application potentially builds and becomes more complicated. We also tried to build the midiplayer notes as the notes were clicked, but didn't have the methods to restart midiplayers once they had already been started and stopped, so that may be another place to refactor.

>Finally, describe how your team collaborated on the project. What did you do together? What did you do separately? What did each team member contribute? Optionally, include a brief team retrospective: What is one thing you did well as a team? What is one thing you could have improved?

After assessing the project, we divied up the project into three components: the red bar, the UI, and the music playing. Cooper started on the red bar, Emma started on the UI, and Ben started on the music playing. After a weekend to work on our seperate parts, we all got together and combined our individual contributions into the repo's master branch. Then we started debugging. A few hours later the code was up to spec and worked as intended. We worked well as a team because we all became experts in the different parts of the code that we had worked on, and accepted suggestions from each other on how to refactor our parts of the project. We could have improved on organizing when we got together; there were miscommunications about when we were working.
