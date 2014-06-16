package com.jd.sparx;

import com.jd.sparx.models.DiagramObjectElement;
import com.jd.sparx.models.IntersectionPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Home PC on 13/06/14.
 */
public class SpatialController {

    private final String GROUP_KEY = "ArchiMate_Grouping";
    private final String FUNCTION_KEY = "ArchiMate_ApplicationFunction";
    private final String COMPONENT_KEY = "ArchiMate_ApplicationComponent";

    private final Map<IntersectionPair, List<DiagramObjectElement>> intersectingComponentMap = new HashMap<IntersectionPair, List<DiagramObjectElement>>();

    public Map<IntersectionPair, List<DiagramObjectElement>> control(final Map<String, List<DiagramObjectElement>> components) {

        /**
         * 1. Work out the intersection dimensions for the group/functions.
         * 2. Add to the DiagramObjectElement with the intersection dimensions.
         * 3. Scan for components which logically fit in the intersection dimensions.
         * 4. Move to the intersections.
         */

        //1 & 2
        final List<DiagramObjectElement> groups = components.get(GROUP_KEY);
        final List<DiagramObjectElement> functions = components.get(FUNCTION_KEY);
        final List<DiagramObjectElement> comps = components.get(COMPONENT_KEY);

        final List<IntersectionPair> ips = new ArrayList<IntersectionPair>();

        for (final DiagramObjectElement g : groups) {
            for (final DiagramObjectElement f : functions) {
                // the function will carry the intersecting spatial dimensions though it should live
                // on both and will be 0..n
                // the IntersectionPair has a pointer to the function and the group
                ips.add(f.calculateIntersection(g));
            }
        }

        // 3
        for (final DiagramObjectElement comp : comps) {
            for (final IntersectionPair ip : ips) {
                if (isPartOfGroupAndFunction(comp, ip)) {
                    addComponentToIntersectionSpace(comp, ip);
                }
            }
        }

        return intersectingComponentMap;
    }

    private boolean isPartOfGroupAndFunction(final DiagramObjectElement comp, final IntersectionPair ip) {

        if(ip == null) return false;
        // get the function and group name from the intersection pair
        final String functionName = ip.getFunction().getElement().GetName();
        final String groupName = ip.getGroup().getElement().GetName();

        final String functions = comp.getElement().GetTaggedValues().GetByName("FUNCTIONS").GetValue();
        final String groups = comp.getElement().GetTaggedValues().GetByName("GROUPS").GetValue();

        final String[] functionArray = functions.split(",");
        final String[] groupArray = groups.split(",");

        for (String function : functionArray) {
            if (function.equals(functionName)) {
                // have a matching function, now look for a group
                for (String group : groupArray) {
                    if (group.equals(groupName)) {
                        System.out.println("Moving [" + comp.getElement().GetName() + "] to function [" + functionName + "] and group [" + groupName + "]");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void addComponentToIntersectionSpace(final DiagramObjectElement comp, final IntersectionPair ip) {
        if(intersectingComponentMap.get(ip) == null) {
            intersectingComponentMap.put(ip, new ArrayList<DiagramObjectElement>());
        }
        intersectingComponentMap.get(ip).add(comp);
        //moveToIntersection(comp, ip);
    }


}
