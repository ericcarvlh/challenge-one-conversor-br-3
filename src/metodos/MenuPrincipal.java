package metodos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import view.conversor.moeda.Moeda;
import view.conversor.sistemaNumerico.Numerico;

public class MenuPrincipal {
	public static void atualizaInterface(JTextField txtHoraAtual, JLabel lblBoasVindas, JTextField txtDataAtual) {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
	        public void run() {
	        	Calendar now = Calendar.getInstance();
	        	TimeZone timezone = TimeZone.getDefault();
	        	now.setTimeZone(timezone);
	        	
	        	DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	        	
	        	dateFormat.setTimeZone(timezone);
	    		
	        	txtHoraAtual.setText(dateFormat.format(now.getTime()));

	        	
	    		int hour = now.get(Calendar.HOUR_OF_DAY);
	    		StringBuilder sb = new StringBuilder(128);
    			
	    		sb.append("<html>");
    			sb.append("<body><p>");
	    		
    			if (hour >= 6 && hour < 12)
	    			sb = sb.append("Bom dia, bem vindo!<br>Já tomou café?");
	    		else if (hour >= 12 && hour < 15)
	    			sb = sb.append("Boa tarde, bem vindo!<br>Já almoçou?");
	    		else if (hour >= 15 && hour < 18)
	    			sb = sb.append("Boa tarde, bem vindo!<br>Já fez o lanchinho da tarde?");
	    		else if (hour >= 18 && hour < 22)
	    			sb = sb.append("Boa noite, bem vindo!<br>Já jantou?");
	    		else
	    			sb = sb.append("Boa madrugada, bem vindo!<br>Já assaltou a geladeira?");

	    		sb.append("</p></body>");
    			sb.append("</html>");
	    		
    			lblBoasVindas.setText(sb.toString());
    			
	        	dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	        	dateFormat.setTimeZone(timezone);
	        	txtDataAtual.setText(dateFormat.format(now.getTime()));
	        }
	    };
		timer.schedule( task, 0L ,1000L);
	}

	public static void acessaFuncionalidade(JFrame formulario, int indexFuncionalidade) {
		if (indexFuncionalidade == 0)
			Moeda.main(null);
		else if (indexFuncionalidade == 1)
			Numerico.main(null);
		
		formulario.dispose();
	}
}
