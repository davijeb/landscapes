package com.jd.sparx;

import com.jd.sparx.models.DiagramObjectElement;
import com.jd.sparx.models.IntersectionPair;

/**
 * Created by Home PC on 13/06/14.
 */
public class SpatialComponentMover {

    public static int[] moveToIntersection(DiagramObjectElement comp, IntersectionPair ip, final int[] pos) {

        final int x = pos[0];
        final int y = -pos[1];
        final int width = comp.getWidth();
        final int height = comp.getHeight();

        comp.getDog().SetLeft(x);
        comp.getDog().SetTop(y);
        comp.getDog().SetRight(x + width);
        comp.getDog().SetBottom(y - height);

        comp.getElement().SetLocked(false);

        // set the sequence to be one less on the z-axis from the function to ensure it is visible
        //comp.getDog().SetSequence(ip.getFunction().getDog().GetSequence()+1);
        comp.getElement().SetParentID(ip.getFunction().getElement().GetElementID());
        comp.getElement().Update();
        comp.getElement().Refresh();
        comp.getDog().Update();

        return new int[]{comp.getDog().GetRight(),comp.getDog().GetTop()};
    }
}
