package com.serkox.entity;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Grid extends JPanel {

    private static boolean game = true;

    private static List<Hexagon> hexagons;

    private final Texture grass;
    private static Texture grass_path;
    private final Texture water;
    private final Texture grass_dark;
    private final Texture textureCapitalIa;
    private final Texture textureCapitalJoueur;
    private final Texture infantry_ia;
    private final Texture infantry_joueur;

    private static Player capitalJoueur;
    private static PNJ capitalIa;

    private final int[][] map = {
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,1,1,1,1,1,1,1,1,1,1,1,0},
            {0,1,2,1,1,1,1,1,1,1,2,2,1,0},
            {0,10,1,2,2,1,1,2,2,1,1,1,0},
            {0,1,1,1,1,1,1,2,1,2,1,1,1,0},
            {0,1,1,1,1,1,2,1,1,1,1,1,0},
            {0,1,1,1,1,2,1,1,1,1,2,2,1,0},
            {0,1,1,1,1,1,1,1,1,1,20,1,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0}

    };

    public Grid(){
        super();

        this.setBounds(0, 50, 1280, 670);
        this.setVisible(true);
        this.setLayout(null);

        this.grass = new Texture("grass");
        grass_path = new Texture("voisin");
        this.grass_dark = new Texture("grass_dark");
        this.water = new Texture("water");
        this.textureCapitalIa = new Texture("capital_ia");
        this.textureCapitalJoueur = new Texture("capital_joueur");
        this.infantry_ia = new Texture("infia");
        this.infantry_joueur= new Texture("infj");
        capitalJoueur = new Player(1);
        capitalIa = new PNJ(2);
        createGrid();
        System.out.println(hexagons.size());
        hexagons.forEach(this::add);
        addNeighbors();
        calculDistanceBetweenHexagonAndCapitalPlayer();
        calculDistanceBetweenHexagonAndCapitalIa();
    }

    public static Texture getGrass_path() {
        return grass_path;
    }

    public static void setGrass_path(Texture grass_path) {
        Grid.grass_path = grass_path;
    }

    public static boolean isGame() {
        return game;
    }

    public static void setGame(boolean game) {
        Grid.game = game;
    }

    /**
     * Génération des hexagons de la grille (n'affiche pas)
     */
    public void createGrid(){
        hexagons = new ArrayList<Hexagon>();

        int y1=0,y2=0,y3=0,y4=0,y5=0,y6=0;
        int x1=0,x2=0,x3=0,x4=0,x5=0,x6=0;
        int lg = 100;
        int id = 0;

        for(int i = 0; i<this.map.length; i++) {

            y1 = ((lg-25)*i);
            y2 = y1+50;
            y3 = ((lg-25)*i)+75;
            y4 = y2;
            y5 = y1;
            y6 = (lg-25)*i-25;

            for (int j = 0; j < this.map[i].length; j++) {

                if(i%2 == 0) {
                    x1 = (lg*j)-50;//-50 en plus
                    x2 = x1;
                    x3 = 50+(lg*j);
                    x4 = x2+100;
                    x5 = x4;
                    x6 = x3;
                }else {
                    x1 = (lg*j);//+50 en moins
                    x2 = x1;
                    x3 = 100+(lg*j);
                    x4 = x2+100;
                    x5 = x4;
                    x6 = x3;
                }
                Hexagon hexagon = new Hexagon(id);
                id +=1;
                hexagon.setPosX(x1);
                hexagon.setPosY(y6);
                if(this.map[i][j] == 1 || this.map[i][j] == 10 ||this.map[i][j] == 20) {
                    hexagon.setBounds(hexagon.getPosX()+5, hexagon.getPosY()+5 , 90, 90);
                }

                //Choix de la texture
                if(this.map[i][j] == 1){
                    hexagon.setObstacle(false);
                    hexagon.setTexture(this.grass);
                    hexagon.setTextureBase(this.grass);
                }else if(this.map[i][j] == 2){
                    hexagon.setObstacle(true);
                    hexagon.setTexture(this.water);
                    hexagon.setTextureBase(this.water);
                }else if(this.map[i][j] == 10){
                    hexagon.setObstacle(false);
                    hexagon.setContainCapital(true);
                    hexagon.setTexture(this.textureCapitalJoueur);
                    hexagon.setTextureBase(this.textureCapitalJoueur);
                    hexagon.setCapital(capitalJoueur);
                    Grid.getCapitalJoueur().setHexagon(hexagon);
                }else if(this.map[i][j] == 20){
                    hexagon.setObstacle(false);
                    hexagon.setContainCapital(true);
                    hexagon.setTexture(this.textureCapitalIa);
                    hexagon.setTextureBase(this.textureCapitalIa);
                    hexagon.setCapital(capitalIa);
                    Grid.getCapitalIa().setHexagon(hexagon);
                }else{
                    hexagon.setObstacle(true);
                    hexagon.setTexture(this.grass);
                    hexagon.setTextureBase(this.grass);
                }

                hexagons.add(hexagon);
                //System.out.println("X : " + hexagon.getPosX() + " Y : " + hexagon.getPosY());
            }
        }

        //System.out.println("Hexagon : " + hexagons.size());
        System.out.println("Grille créé !");
    }

    public List<Hexagon> getHexagons() {
        return hexagons;
    }

    public void setHexagons(List<Hexagon> hexagons) {
        hexagons = hexagons;
    }

    protected void paintComponent(Graphics g){

        super.paintComponent(g);
        Graphics g2d = g;
        if(game) {
            int cpt = 0;

            for (int i = 0; i < this.map.length; i++) {

                for (int j = 0; j < this.map[i].length; j++) {
                    Hexagon hexagon = hexagons.get(cpt);
                    cpt++;

                    if (this.map[i][j] != 0) {

                        g2d.setColor(Color.black);

                        if (hexagon.isContainCapital()) {
                            g2d.drawImage(hexagon.getTexture().getTexture(), hexagon.getPosX(), hexagon.getPosY(), 100, 100, null);
                        } else {
                            g2d.drawImage(hexagon.getTexture().getTexture(), hexagon.getPosX(), hexagon.getPosY(), 100, 100, null);
                        }

                        //Affichage des unités
                        if (hexagon.getUnit() != null) {
                            if(Grid.getCapitalJoueur().getUnitCollection().contains(hexagon.getUnit())){
                                g2d.drawImage(this.infantry_joueur.getTexture(), hexagon.getPosX(), hexagon.getPosY(), 100, 100, null);
                            }else{
                                g2d.drawImage(this.infantry_ia.getTexture(), hexagon.getPosX(), hexagon.getPosY(), 100, 100, null);

                            }

                            //g2d.setColor(Color.red);
                            //g2d.drawString(String.valueOf(hexagon.getUnit().getId()), hexagon.getPosX() + 40, hexagon.getPosY() + 60);

                            g2d.setColor(Color.yellow);
                            g2d.drawString(String.valueOf(hexagon.getUnit().getHealth()), hexagon.getPosX() + 40, hexagon.getPosY() + 20);


                            g2d.setColor(Color.black);

                        }
                    } else {
                        g2d.drawImage(this.grass_dark.getTexture(), hexagon.getPosX(), hexagon.getPosY(), 100, 100, null);
                    }

                    g2d.setFont(new Font("default", Font.BOLD, 16));
                    //g2d.drawString(String.valueOf(hexagon.getId() + " " + hexagon.getDistancePlayer()), hexagon.getPosX() + 40, hexagon.getPosY() + 40);
                    //g2d.drawString(String.valueOf(hexagon.getDistanceIa()), hexagon.getPosX()+40, hexagon.getPosY()+80);
                    //g2d.drawString(String.valueOf(hexagon.getDistanceCapital()), hexagon.getPosX() + 40, hexagon.getPosY() + 80);
                    //g2d.drawString(String.valueOf("prio " + hexagon.getDistancePriorityHexagon()), hexagon.getPosX()+40, hexagon.getPosY()+80);
                }
            }
        }
    }

    public static Player getCapitalJoueur() {
        return capitalJoueur;
    }

    public static PNJ getCapitalIa() {
        return capitalIa;
    }

    public static void resetTextureHexagons(){
        for(Hexagon hexagon : hexagons){
            hexagon.setTexture(hexagon.getTextureBase());
        }
    }

    public static void resetViewIaHexagons() {
        for(Hexagon hexagon : hexagons){
            hexagon.setViewIa(false);
        }
    }

    public static void resetViewCapitalIaHexagons() {
        for(Hexagon hexagon : hexagons){
            hexagon.setViewCapital(false);
        }
    }

    public static void resetViewHexagons(){
        for(Hexagon hexagon : hexagons){
            hexagon.setViewPlayer(false);
        }
    }

    public static void resetViewRadiusHexagons(){
        for(Hexagon hexagon : hexagons){
            hexagon.setViewRadius(false);
        }
    }

    public static void resetViewPriorityHexagons(){
        for(Hexagon hexagon : hexagons){
            hexagon.setViewPriorityHexagon(false);
        }
    }

    /**
     * Ajout des voisins a chaque hexagon
     */
    public void addNeighbors(){
        int cpt = 0;

        for(int i = 0; i<this.map.length; i++){

            for(int j = 0; j<this.map[i].length; j++){

                Hexagon currentHexagon = hexagons.get(cpt);
                //System.out.println("Id " + currentHexagon.getId());
                ArrayList<Hexagon> ng = new ArrayList<Hexagon>();
                int v1, v2, v3, v4, v5, v6;
                Hexagon voisin1, voisin2, voisin3, voisin4, voisin5, voisin6;
                //-13 -14 +13 +14 +1 -1

                //Ajout des 2 voisins au nord
                if(i>=1){
                    v1 = currentHexagon.getId() - 13;
                    v2 = currentHexagon.getId() - 14;
                    voisin1 = hexagons.get(v1);
                    voisin2 = hexagons.get(v2);
                    ng.add(voisin1);
                    ng.add(voisin2);
                    //System.out.println("Voisin 1 : " + voisin1.getId());
                    //System.out.println("Voisin 2 : " + voisin2.getId());
                }

                //Ajout des voisins droit et gauche
                if(j >= 1 && j < this.map[i].length-1){
                    v3 = currentHexagon.getId() - 1;
                    voisin3 = hexagons.get(v3);
                    ng.add(voisin3);
                    //System.out.println("Voisin 3 : " + voisin3.getId());
                    v4 = currentHexagon.getId() + 1;
                    voisin4 = hexagons.get(v4);
                    ng.add(voisin4);
                    //System.out.println("Voisin 4 : " + voisin4.getId());

                    /*v3 = currentHexagon.getPosX() + 100;
                    v4 = currentHexagon.getPosX() - 100;
                    voisin3 = (Hexagon) hexagons.stream().filter(hex -> hex.getPosX() == v3).map(Hexagon::getId);
                    ng.add(voisin3);*/

                }

                //Ajout des 2 voisins au sud
                if(i < this.map.length-1){
                    v5 = currentHexagon.getId() + 13;
                    v6 = currentHexagon.getId() + 14;
                    voisin5 = hexagons.get(v5);
                    voisin6 = hexagons.get(v6);
                    ng.add(voisin5);
                    ng.add(voisin6);
                    //System.out.println("Voisin 5 : " + voisin5.getId());
                    //System.out.println("Voisin 6 : " + voisin6.getId());
                }
                cpt++;


                for(int k = 0; k<ng.size(); k++){

                    if(/*!ng.get(k).isContainCapital() &&*/ !ng.get(k).isObstacle()) {
                        currentHexagon.addNeighbor(ng.get(k));
                    }

                }

            }


        }

    }

    public void calculDistanceBetweenHexagonAndCapitalIa(){

        Hexagon hexagonDepart = Grid.getCapitalIa().getHexagon();

        hexagonDepart.setViewIa(true);
        hexagonDepart.setDistanceBetweenCapitalIa(0);

        List<Hexagon> file = new ArrayList<Hexagon>();
        file.add(hexagonDepart);

        while (file.size() != 0){

            Hexagon hexagon = file.get(0);
            List<Hexagon> neighbors = hexagon.getNeighbors();

            for (Hexagon neighbor : neighbors) {

                if (neighbor != null) {
                    if (!neighbor.getViewIa() && !neighbor.isObstacle() && !neighbor.isContainCapital() && neighbor.getUnit() == null) {
                        neighbor.setViewIa(true);
                        neighbor.setDistanceBetweenCapitalIa(hexagon.getDistanceBetweenCapitalIa() + 1);
                        //System.out.println(neighbor.getId() + " : " + neighbor.getDistanceBetweenCapitalIa());
                        file.add(neighbor);
                    }

                }
            }
            file.remove(0);
        }
    }

    public void calculDistanceBetweenHexagonAndCapitalPlayer(){

        Hexagon hexagonDepart = Grid.getCapitalJoueur().getHexagon();

        hexagonDepart.setViewPlayer(true);
        hexagonDepart.setDistanceBetweenCapitalJoueur(0);

        List<Hexagon> file = new ArrayList<Hexagon>();
        file.add(hexagonDepart);

        while (file.size() != 0){

            Hexagon hexagon = file.get(0);
            List<Hexagon> neighbors = hexagon.getNeighbors();

            for (Hexagon neighbor : neighbors) {

                if (neighbor != null) {
                    if (!neighbor.getViewPlayer() && !neighbor.isObstacle() && !neighbor.isContainCapital() && neighbor.getUnit() == null) {
                        neighbor.setViewPlayer(true);
                        neighbor.setDistanceBetweenCapitalJoueur(hexagon.getDistanceBetweenCapitalJoueur() + 1);
                        //System.out.println(neighbor.getId() + " : " + neighbor.getDistanceBetweenCapitalJoueur());
                        file.add(neighbor);
                    }

                }
            }
            file.remove(0);
        }
    }
}