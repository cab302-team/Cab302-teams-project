package com.example.project.controllers.gameScreens;

import static org.mockito.Mockito.mock;

class GameScreenFactoryTest
{
//    @Test
//    void intitialise_FirstTimeSuccess()
//    {
//        RootLayoutController controller = new RootLayoutController();
//        Map<GameScenes, GameScreenController> controllerMap = new HashMap<>();
//        Map<GameScenes, Parent> pagesMap = new HashMap<>();
//
//        var sceneManager = new SceneManager(null, controllerMap, pagesMap);
//
//        var mockLoader = mock(PageLoader.class);
//
//        try{
//            when(mockLoader.load(anyString())).thenReturn(new StackPane());
//        }
//        catch (IOException e){
//            Assertions.fail();
//        }
//
//        when(mockLoader.getController()).thenReturn(mock(GameScreenController.class));
//        sceneManager.initialise(controller, mockLoader);
//
//        // assert pages has 3 entries, assert controllers have 3 entries.
//        assertEquals(5, controllerMap.size());
//        assertEquals(5, pagesMap.size());
//    }
//
//    @Test
//    void initialise_ThrowsIfCalledTwice()
//    {
//        RootLayoutController rootController = new RootLayoutController();
//        Map<GameScenes, GameScreenController> controllerMap = new HashMap<>();
//        Map<GameScenes, Parent> pagesMap = new HashMap<>();
//        var sceneManager = new SceneManager(null, null, controllerMap, pagesMap);
//        var mockLoader = mock(PageLoader.class);
//
//        try{
//            when(mockLoader.load(anyString())).thenReturn(new StackPane());
//        }
//        catch (IOException e){
//            Assertions.fail();
//        }
//
//        when(mockLoader.getController()).thenReturn(mock(GameScreenController.class));
//        sceneManager.initialise(rootController, mockLoader);
//
//        // Correct - passes executable that assertThrows will call
//        assertThrows(RuntimeException.class, () ->
//                sceneManager.initialise(rootController, new FXMLPageLoader())
//        );
//    }
}