//import javax.swing.*;
//
//public class EnemyController implements Runnable {
//    private EnemyModel model;
//    private EnemyView view;
//    private Play playController;
//    private boolean running = true;
//
//    public EnemyController(Play playController, int panelWidth, int panelHeight) {
//        this.model = new EnemyModel(panelWidth, panelHeight);
//        this.view = new EnemyView();
//        this.playController = playController;
//
//        SwingUtilities.invokeLater(() -> {
//            playController.getPanel().add(view);
//        });
//    }
//
//    @Override
//    public void run() {
//        while (running && model.isAlive()) {
//            model.move();
//
//            SwingUtilities.invokeLater(() -> {
//                view.updatePosition(model.getX(), model.getY());
//            });
//
//            if (model.checkCollision()) {
//                playController.reducePlayerHealth();
//                break;
//            }
//
//            try {
//                Thread.sleep(30);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                break;
//            }
//        }
//
//        playController.removeEnemy(this);
//    }
//
//    public void stop() {
//        running = false;
//    }
//
//    public EnemyView getView() { return view; }
//}