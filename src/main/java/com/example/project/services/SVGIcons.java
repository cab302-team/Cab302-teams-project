package com.example.project.services;

import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

/**
 * a static class to hold some icons for the application.
 */
public class SVGIcons
{
    private static final String cogIcon = "M19.43 12.98c.04-.32.07-.66.07-1s-.03-.68-.07-1l2.11-1.65c.19-.15.24-.42.12-.64l-2-3.46c-.12-.21-.37-.3-.6-.22l-2.49 1a7.027 7.027 0 00-1.73-1l-.38-2.65A.495.495 0 0014 2h-4c-.25 0-.46.18-.49.42l-.38 2.65c-.63.24-1.21.57-1.73 1l-2.49-1a.5.5 0 00-.6.22l-2 3.46c-.12.21-.07.49.12.64l2.11 1.65c-.04.32-.07.66-.07 1s.03.68.07 1l-2.11 1.65a.5.5 0 00-.12.64l2 3.46c.12.21.37.3.6.22l2.49-1c.52.43 1.1.77 1.73 1l.38 2.65c.03.24.24.42.49.42h4c.25 0 .46-.18.49-.42l.38-2.65c.63-.24 1.21-.57 1.73-1l2.49 1c.23.09.49 0 .6-.22l2-3.46c.12-.21.07-.49-.12-.64l-2.11-1.65zM12 15.5a3.5 3.5 0 110-7 3.5 3.5 0 010 7z";

    /**
     * gets a cog icon.
     * @return icon SVG.
     */
    public static SVGPath getCogIcon()
    {
        SVGPath cog = new SVGPath();
        cog.setContent(cogIcon);
        cog.setFill(Color.GRAY);
        return cog;
    }
}
