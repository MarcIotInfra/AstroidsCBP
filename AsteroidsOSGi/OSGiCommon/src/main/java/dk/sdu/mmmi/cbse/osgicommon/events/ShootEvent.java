/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.osgicommon.events;

import dk.sdu.mmmi.cbse.osgicommon.data.Entity;



/**
 *
 * @author Marc
 */
public class ShootEvent extends Event{
    
    public ShootEvent(Entity source) {
        super(source);
    }
    
}
