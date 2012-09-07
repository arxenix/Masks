package com.github.bobacadodl.Masks;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;

public class MasksMapRenderer extends MapRenderer{
	

	Masks masks;
	String pname;

	public MasksMapRenderer (Masks masks, String pname){
		super (true); 
		this.masks = masks;
		this.pname =  pname;
	}


	@Override
	public void render(MapView map, MapCanvas canvas, Player p) {
        for (int j = 0; j < 128; j++) 
            for (int i = 0; i < 128; i++)
              canvas.setPixel(i, j, (byte) 0);
        //if (plg.dtimg != null) canvas.drawImage(0, 0, plg.dtimg);
        try {
            // Create a URL for the image's location
            URL url = new URL("http://minotar.net/avatar/"+p.getName()+"/64");

            java.awt.Toolkit.getDefaultToolkit();
			// Get the image
            Image image = ImageIO.read(url);
            canvas.drawImage(32, 16, image);
        } catch (IOException e) {
        	e.printStackTrace();
        }
        
        canvas.drawText(8, 90, MinecraftFont.Font, pname);
        canvas.drawText(8, 115, MinecraftFont.Font, "¤54;Masks by bobacadodl");
	}
	

}
