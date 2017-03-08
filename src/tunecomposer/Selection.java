/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

global Note[] listofallNotes;
//When a Note is created it should be added to the global array "listofallNotes"
import java.util.ArrayList;
import java.awt.Point;

/**
 *
 * @author lonbern
 */


/* METHODS OF "NOTE" THAT NEED TO BE IMPLEMENTED FOR SELECTION TO WORK
   void mark(): creates a red box around the note.
   void unmark(): removes the red box from the note.
   void resize(int xChange): resizes the Note given a number of pixels to adjust right side horizontally.
   void drag(int xChange, int yChange): moves the location of the Note given x and y pixel shifts.
   boolean isInBox(Point topLeft, Point bottomRight): returns true if any part of the Note is contained in the selection box given the box's corner Points
   void delete(): delete the Note and remove it from the global Array "listofallNotes".
*/

public class Selection {
    
    ArrayList<Note> selected;
    ArrayList<Note> notesInBox;
    
    
    void unselect(Note...notesClick){
        selected.removeAll(notesClick);
        for (Note s: notesClick){
            s.unmark();
        }
    }
    
    void deleteSelected(){
        for (Note d: selected){
            unselect(d);
            d.delete();
        }
    }
    
    void select(Note...notesClick){
        selected.addAll(notesClick);
        for (Note s: notesClick){
            s.mark();
        }
    }
    
    void unselectReplace(Note...notesClick){
        unselect();
        select(notesClick);
    }
    
    void resizeSelected(int xChange){
        for (Note s: selected){
            s.resize(xChange);
        }
    }
    
    void dragSelected(int xChange, int yChange){
        for (Note s: selected){
            s.drag(xChange,yChange);
        }
    }
    
    Note[] boxScan(Point topLeft, Point bottomRight, boolean ctrl){
        for (Note b: listofAllNotes){
            if (b.isInBox(topLeft,bottomRight)){
                if (!ctrl || !selected.contains(b)){
                    notesInBox.add(b);
                }
            }
            else if (notesInBox.contains(b)){
                notesInBox.remove(b);
                unselect(b);
            }
        }
        
        for (Note c: notesInBox){
            c.mark();
        }
        
        return notesInBox.toArray();
    }
    
    void boxSelectionSubmit(Note[] boxedNotes){
        notesInBox.clear();
        select(boxedNotes);
    }
    
    
}