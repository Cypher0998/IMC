package dad.maven.imc;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

public class Imc extends Application {
	private Label peso,altura,kg,cm,imcLabel,resultado;
	private TextField textoAltura,textoPeso;
	private HBox cajaPeso,cajaAltura,cajaFormula;
	private VBox view;
	@SuppressWarnings("unused")
	private Scene escena;
	private Label estado = new Label();
	DoubleProperty pesoImc;
	DoubleProperty alturaImc;
	DoubleProperty calculoImc;
	StringExpression aux;
	
	public void start(Stage primaryStage) throws Exception {
		startPeso();
		startAltura();
		startImcLabel();
		cajaPeso();
		cajaAltura();
		cajaFormula();
		view();
		bindingCalculo();
		Scene escena=new Scene(view,320,200);
		primaryStage.setScene(escena);
		primaryStage.setTitle("Calculadora IMC");
		primaryStage.show();
	}
	@SuppressWarnings("unchecked")
	private void bindingCalculo() {
		pesoImc= new SimpleDoubleProperty();
		alturaImc= new SimpleDoubleProperty();
		calculoImc=new SimpleDoubleProperty();
		calculoImc.bind(pesoImc.divide(alturaImc.multiply(alturaImc).divide(10000)));
		StringConverter<? extends Number > lector= new DoubleStringConverter();
		Bindings.bindBidirectional(textoPeso.textProperty(), pesoImc, (StringConverter<Number>)lector);
		Bindings.bindBidirectional(textoAltura.textProperty(), alturaImc, (StringConverter<Number>)lector);
		aux=Bindings
				.when(Bindings.isEmpty(textoAltura.textProperty()).and(Bindings.isEmpty(textoPeso.textProperty())))
				.then("(peso * altura^2)")
				.otherwise(Bindings
						.when(alturaImc.isEqualTo(0))
						.then("Inserta altura por favor.").otherwise(calculoImc.asString()));
		resultado.textProperty().bind(aux);
		resultado.textProperty().addListener(e->{
			if(calculoImc.doubleValue()==0 || alturaImc.doubleValue()<=0)
				estado.setText("Introduce valores para calcular");
			else if(calculoImc.doubleValue()<18.5 && calculoImc.doubleValue()>0)
				estado.setText("bajo peso");
			else if(calculoImc.doubleValue()>=18.5 && calculoImc.doubleValue()<25)
				estado.setText("Peso normal");
			else if(calculoImc.doubleValue()>=25 && calculoImc.doubleValue()<30)
				estado.setText("Sobrepeso");
			else if(calculoImc.doubleValue()>=30)
				estado.setText("Obeso");
		});
	}
	private void startPeso() {
		peso=new Label();
		peso.setText("Peso");
		textoPeso=new TextField();
		kg=new Label();
		kg.setText("kg");
	}
	private void startAltura() {
		altura=new Label();
		altura.setText("Altura");
		textoAltura=new TextField();
		cm=new Label();
		cm.setText("cm");
	}
	private void startImcLabel() {
		imcLabel=new Label();
		imcLabel.setText("IMC: ");
		resultado=new Label();
	}
	private void cajaPeso() {
		cajaPeso=new HBox(5);
		cajaPeso.getChildren().addAll(peso,textoPeso,kg);
		cajaPeso.setAlignment(Pos.CENTER);
	}
	private void cajaAltura() {
		cajaAltura=new HBox(5);
		cajaAltura.getChildren().addAll(altura,textoAltura,cm);
		cajaAltura.setAlignment(Pos.CENTER);
	}
	private void cajaFormula() {
		cajaFormula=new HBox(5);
		cajaFormula.getChildren().addAll(imcLabel,resultado);
		cajaFormula.setAlignment(Pos.CENTER);
	}
	private void view() {
		view=new VBox(5);
		view.getChildren().addAll(cajaPeso,cajaAltura,cajaFormula,estado);
		view.setAlignment(Pos.CENTER);
	}
	
	
	public static void main(String[] args) {
		launch(args);

	}

}
