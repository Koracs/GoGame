package com.gogame;


import com.gogame.view.*;
import com.gogame.controller.*;
import com.gogame.model.*;


public class Main{
    public static void main(String[] args) {
        GoBoardController controller = new GoBoardController();
        GoBoardView view = new GoBoardView();
        GoBoardModel model = new GoBoardModel();

        model.registerView(view);
        controller.setModel(model);
        view.setActionListener(controller);

        //GoBoardView.launch(GoBoardView.class,args);
    }
}
