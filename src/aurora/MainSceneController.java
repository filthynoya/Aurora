package aurora;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

public class MainSceneController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Menu mainMenu;

    @FXML
    private MenuItem newOption;

    @FXML
    private MenuItem openOption;

    @FXML
    private MenuItem saveOption;

    @FXML
    private MenuItem aboutOption;

    @FXML
    private TextField sizeField;

    @FXML
    private Button setSizeButton;

    @FXML
    private ColorPicker colorPick;

    @FXML
    private RadioButton fillSelected;

    @FXML
    private RadioButton strokeSelected;

    @FXML
    private Label modeString;

    @FXML
    private Button brushButton;

    @FXML
    private Button bucketButton;

    @FXML
    private Button pencilButton;

    @FXML
    private Button eraserButton;

    @FXML
    private Button recButton;

    @FXML
    private Button circleButton;
    
    @FXML
    private Button selectionButton;
    
    @FXML
    private Button starButton;

    @FXML
    private Canvas canvas;

    
    //Global Variables
    static final int BrushMode = 1;
    static final int BucketMode = 2;
    static final int PencilMode = 3;
    static final int EraserMode = 4;
    static final int RectangleMode = 5;
    static final int CircleMode = 6;
    static final int StarMode = 7;
    static final int SelectionMode = 8;
    static final int FillMode = 1;
    static final int StrokeMode = 2;
    static final int SelectedMode = 0;
    static final int ReleasedMode = 1;
    static final int DefaultSize = 12;
    
    static int mode = BrushMode;
    static int fillMode = FillMode;
    static int selectionMode = SelectedMode;
    static Color color;
    static int size = DefaultSize;
    static int pressedX;
    static int pressedY;
    static int releasedX;
    static int releasedY;
    
    private GraphicsContext gc;
    
    @FXML
    void initialize() {
	gc = canvas.getGraphicsContext2D();
	modeString.setText("Brush");
	resetCanvas();
    }
    
    int min (int x, int y) {
        if (x <= y) {
            return x;
        } else {
            return y;
        }
    }
    
    int max (int x, int y) {
        if (x >= y) {
            return x;
        } else {
            return y;
        }
    }
    
    @FXML
    void colorSelected(ActionEvent event) {
	color = colorPick.getValue();
    }

    @FXML
    void selectedBrush(ActionEvent event) {
	mode = BrushMode;
	modeString.setText("Brush");
    }

    @FXML
    void selectedBucket(ActionEvent event) {
	mode = BucketMode;
	modeString.setText("Bucket");
    }
    
    @FXML
    void selectedPencil(ActionEvent event) {
	mode = PencilMode;
	modeString.setText("Pencil");
    }
    
    @FXML
    void newFile(ActionEvent event) {
	resetCanvas();
    }
    
    @FXML
    void openFile(ActionEvent event) {
	resetCanvas();
	
	FileChooser fc = new FileChooser ();
	fc.setInitialDirectory(new File ("../"));
	fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG","*.png"));
	fc.setTitle("Open Image");
	
	File file = fc.showOpenDialog(canvas.getScene().getWindow());
	
	if (file != null) {
	    Image image = new Image (file.toURI().toString());
	    
	    gc.drawImage(image, 0, 0);
	}
    }

    @FXML
    void saveFile(ActionEvent event) {
	FileChooser fc = new FileChooser ();
	fc.setInitialDirectory(new File ("../"));
	fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG","*.png"));
	fc.setTitle("Save Image");
	
	File file = fc.showSaveDialog(canvas.getScene().getWindow());
	
	if (file != null) {
	    WritableImage wt = new WritableImage ((int) canvas.getWidth(), (int) canvas.getHeight());
	    
	    try {
		ImageIO.write (SwingFXUtils.fromFXImage (canvas.snapshot (null,wt), null), "png", file);
	    } catch (IOException ex) {
		Logger.getLogger(MainSceneController.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }
    
    @FXML
    void selectedCircle(ActionEvent event) {
	mode = CircleMode;
	modeString.setText("Circle");
    }

    @FXML
    void selectedEraser(ActionEvent event) {
	mode = EraserMode;
	modeString.setText("Eraser");
    }

    @FXML
    void selectedFill(ActionEvent event) {
	fillMode = FillMode;
	strokeSelected.setSelected(false);
	fillSelected.setSelected(true);
	fillSelected.setDisable(true);
	strokeSelected.setDisable(false);
    }
    
    @FXML
    void selectedRec(ActionEvent event) {
	mode = RectangleMode;
	modeString.setText("Rectangle");
    }
    
    @FXML
    void selectedSelection(ActionEvent event) {
        mode = SelectionMode;
        selectionMode = SelectedMode;
        modeString.setText("Selection");
    }

    @FXML
    void selectedStroke(ActionEvent event) {
	fillMode = StrokeMode;
	strokeSelected.setSelected(true);
	fillSelected.setSelected(false);
	fillSelected.setDisable(false);
	strokeSelected.setDisable(true);
    }
    
    @FXML
    void selectedStar(ActionEvent event) {
        mode = StarMode;
	modeString.setText("Star");
    }

    @FXML
    void showAbout(ActionEvent event) {
	Alert alert = new Alert(AlertType.INFORMATION);
	alert.setTitle("About Us");
	alert.setHeaderText("Thank You For Using Aurora.");
	alert.setContentText("This small project was made by Nowratun Oyshe (190204056) and "
		+ "Ayon Raihan Barno (190204057) for our lab course CSE2100\n\n\n"
		+ "From AlphaCoders, Aust with Love");
	
	alert.initOwner (canvas.getScene().getWindow());

	alert.showAndWait();
    }
    
    @FXML
    void setSizePressed(ActionEvent event) {
	String text = sizeField.getText();
	
	try {
	    size = Integer.parseInt(text);
	} catch (NumberFormatException ex) {
	    size = DefaultSize;
	}
	    
	if (size <= 0 || size >= 100) {
	    size = DefaultSize;
	}
	
	sizeField.setText (Integer.toString (size));
    }
    
    @FXML
    void setOnMouseDragged(MouseEvent event) {
	if (mode == BrushMode) {
	    double sz = size;
            double x = event.getX() - sz / 2;
            double y = event.getY() - sz / 2;
                
            gc.setFill (color);
            gc.fillRoundRect (x, y, sz, sz, sz, sz);
	}
	if (mode == PencilMode) {
	    gc.lineTo (event.getX(),event.getY());
	    gc.stroke ();
	}
	
	if (mode == EraserMode) {
	    double sz = size;
            double x = event.getX() - sz / 2;
            double y = event.getY() - sz / 2;
                
            gc.setFill (Color.WHITE);
            gc.fillRoundRect (x, y, sz, sz, sz, sz);
	}
    }

    @FXML
    void setOnMousePressed(MouseEvent event) {
        if (mode == SelectionMode) {
            if (selectionMode == ReleasedMode) {
                int startX = min (pressedX, releasedX);
                int startY = min (pressedY, releasedY);
                
                int endX = min ((int) canvas.getWidth () - 1, max (pressedX, releasedX));
                int endY = min ((int) canvas.getHeight() - 1, max (pressedY, releasedY));
                
                PixelWriter pw = gc.getPixelWriter();
                PixelReader pr = canvas.snapshot(null, null).getPixelReader();
                
                int newX = (int) event.getX () - ((endX - startX) / 2);
                int newY = (int) event.getY () - ((endY - startY) / 2);
                
                for (int i = startX, k = newX; i <= endX; i++, k++) {
                    for (int j = startY, l = newY; j <= endY; j++, l++) {
                        Color cc = pr.getColor(i, j);
                        pw.setColor(i, j, Color.WHITE);
                        pw.setColor(k, l, cc);
                    }
                }
                
                selectionMode = SelectedMode;
            }
        }
        
        pressedX = (int) event.getX ();
        pressedY = (int) event.getY ();
        
	if (mode == BrushMode) {
	    double sz = size;
            double x = event.getX() - sz / 2;
            double y = event.getY() - sz / 2;
                
            gc.setFill (color);
            gc.fillRoundRect (x, y, sz, sz, sz, sz);
	}
	if (mode == PencilMode) {
	    gc.setStroke (color);
	    gc.beginPath ();
	}
	
	if (mode == EraserMode) {
	    double sz = size;
            double x = event.getX() - sz / 2;
            double y = event.getY() - sz / 2;
                
            gc.setFill (Color.WHITE);
            gc.fillRoundRect (x, y, sz, sz, sz, sz);
	}
        
        if (mode == StarMode) {
            double[] xpoints = {0, 75, 100, 125, 200, 150, 160, 100, 40, 50};
            double[] ypoints = {75, 65, 0, 65, 75, 115, 180, 140, 180, 115};
            
            double scale = ((double) size / 100.0);
            double offsetX = pressedX - ((xpoints[4] / 2.0) * scale);
            double offsetY = pressedY - ((ypoints[6] / 2.0) * scale);
            
            for (int i = 0; i < xpoints.length; i++) {
                xpoints[i] *= scale;
                ypoints[i] *= scale;
                xpoints[i] += offsetX;
                ypoints[i] += offsetY;
            }
            
            if (fillMode == FillMode) {
                gc.setFill(color);
                gc.fillPolygon (xpoints, ypoints, xpoints.length);
            } else {
                gc.setStroke(color);
                gc.strokePolygon (xpoints, ypoints, xpoints.length);
            }
        }
    }

    @FXML
    void setOnMouseReleased(MouseEvent event) {
        releasedX = (int) event.getX ();
        releasedY = (int) event.getY ();
        
	if (mode == BucketMode) {
	    gc.setFill(color);
	    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
	if (mode == PencilMode) {
	    gc.closePath ();
            
            if (fillMode == FillMode) {
                gc.setFill(color);
                gc.fill();
            }
	}
	
	if (mode == RectangleMode) {
	    if(fillMode == FillMode){
            int startX = min (pressedX, releasedX);
                int startY = min (pressedY, releasedY);
                
                int endX = max (pressedX, releasedX);
                int endY = max (pressedY, releasedY);
                int width = endX-startX;
                int height = endY - startY;
                
                gc.setFill(color);
                gc.fillRect(startX, startY, width, height);
            } else if(fillMode == StrokeMode) {
                int startX = min (pressedX, releasedX);
                int startY = min (pressedY, releasedY);

                int endX = max (pressedX, releasedX);
                int endY = max (pressedY, releasedY);
                int width = endX-startX;
                int height = endY - startY;

                gc.setStroke(color);
                gc.strokeRect(startX, startY, width, height);

            }
	}
	
	if (mode == CircleMode) {
	    if(fillMode == FillMode) {
                int startX = min (pressedX, releasedX);
                int startY = min (pressedY, releasedY);
                
                int endX = max (pressedX, releasedX);
                int endY = max (pressedY, releasedY);
                int width = endX-startX;
                int height = endY - startY;
                int arcWidth=endX-startX;
                int arcHeight=endY - startY;
                
                
                gc.setFill(color);
                gc.fillRoundRect(startX, startY, width, height, arcWidth, arcHeight);
            } else if(fillMode == StrokeMode) {
                int startX = min (pressedX, releasedX);
                int startY = min (pressedY, releasedY);
                
                int endX = max (pressedX, releasedX);
                int endY = max (pressedY, releasedY);
                int width = endX-startX;
                int height = endY - startY;
                int arcWidth=endX-startX;
                int arcHeight=endY - startY;
                
                
                gc.setStroke(color);
                gc.strokeRoundRect(startX, startY, width, height, arcWidth, arcHeight);
            }
	}
        
        if (mode == SelectionMode) {
            if (selectionMode == SelectedMode) {
                selectionMode = ReleasedMode;
            }
        }
    }
    
    void resetCanvas () {
	sizeField.setText (Integer.toString (size));
	gc.setFill(Color.WHITE);
	gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	color = colorPick.getValue();
    }
}
