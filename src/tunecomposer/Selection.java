/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.ArrayList;
import java.awt.Point;
import tunecomposer.TuneComposer;
import tunecomposer.NoteBox;

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
    
    ArrayList<NoteBox> selected;
    ArrayList<NoteBox> notesInRect;
    
    
    void unselect(NoteBox...notesClick){
        selected.removeAll(notesClick);
        for (NoteBox s: notesClick){
            s.unmarkNote();
        }
    }
    
    void deleteSelected(){
        for (NoteBox d: selected){
            TuneComposer.deleteNote(d);
            unselect(d);
            d.delete();
        }
    }
    
    void select(NoteBox...notesClick){
        selected.addAll(notesClick);
        for (NoteBox s: notesClick){
            s.markNote();
        }
    }
    
    void unselectReplace(NoteBox...notesClick){
        unselect();
        select(notesClick);
    }
    
    void resizeSelected(int xChange){
        for (NoteBox s: selected){
            s.resize(xChange);
        }
    }
    
    void dragSelected(int xChange, int yChange){
        for (NoteBox s: selected){
            s.drag(xChange,yChange);
        }
    }
    
    NoteBox[] rectScan(Point topLeft, Point bottomRight, boolean ctrl, NoteBox[] listofNotes){
        for (NoteBox b: listofAllNotes){
            if (b.isInRect(topLeft,bottomRight)){
                if (!ctrl || !selected.contains(b)){
                    notesInRect.add(b);
                }
            }
            else if (notesInRect.contains(b)){
                notesInRect.remove(b);
                unselect(b);
            }
        }
        
        for (NoteBox c: notesInRect){
            c.markNote();
        }
        
        return notesInRect.toArray();
    }
    
    void boxSelectionSubmit(NoteBox[] notesToSel){
        notesInRect.clear();
        select(notesToSel);
    }
    
    
}