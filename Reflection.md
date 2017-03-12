## Reflection
#### by Quinn, Ben, Will, and Niki

1) In terms of instrument selection, we just had a single global variable that would be changed based on which radio button was currently selected. We then used that variable as an input when constructing a new note.

We created a NoteBox class to keep the information contained in each note for easy access and mutability. We also created a selection box global rectangle to keep track of the bow that appears during a click and drag.

Overall, most of our functionality comes from event handles connected to our FXML file, breaking down what happens during different mouse events and button presses.

2) For the instrument panel this was the best way to do it as it hardly impacted our previous method for note creation, and creating the NoteBox class meant we could wrap up a lot of methods to be called by the controller class without adding as much clutter to that class.


3) Ideally each radio button would be contained in a ToggleGroup. This would automatically mean only one radio button could be selected at a certain time. Unfortunately when using the ToggleGroup we couldn't figure out how to keep the vertical layout of the panel, and so opted to just have a couple lines of code to deselect the other buttons manually.

Additionally a lot of our code to do with selection is messy in general. This is because we were struggling really hard with the functional requirements, and got to the point where we just needed to get as much functionality done as we could. We also had a selector class at one point, but the remaining people able to work on the project were unable to figure out how it was supposed to be used and it was scraped in favor of methods in the controller class.

With only two of us left we simply were not able to figure out how to properly drag notes and resize them. We came close, but couldn’t figure out why the notes were moving faster than the mouse. 

4)
Quinn/Will collaborated on most of the code that had to do with the selection rectangle interacting with the NoteBoxes

Quinn/Ben collaborated on getting the instrument pane to change the construction of note boxes, and getting different instrument noises to play

Quinn - Instrument Pane, selection methods of note box objects, UML diagram.
Will - created the Selection Rectangle and figured out how to use drag events.
Ben - Created the NoteBox class, and several methods to interact with them in the controller class
Niki - Created the unused selection class (was really sick).

5)
Our estimation was pretty optimistic. During the planning poker activity we estimated that most, if not all, of the smaller tasks would take about 30 minutes, which if we broke it up wouldn't be too bad. Indeed, most of the smaller tasks took about the 30 minutes we had estimated, but a couple of them - specifically implementing dragging and resizing notebars - proved a much more difficult task. Our estimation on the larger portions and code design were not too far off. The problem became the refactoring of the code mid-way through that effectively doubled the time it took to build the code organization. In total, we need to improve on understanding the intricacies of smaller tasks to better estimating the time they will take and need to either plan out or code better or budget in time for large scale refactoring.
