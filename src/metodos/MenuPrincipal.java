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
	
	/**
	 * Atualiza a interface a cada 1 segundo setando e atualizando algumas informa??es.
	 * @author Eric Carvalho
	 * @param textFieldHoraAtual ? o campo onde vai a hora atual.
	 * @param lblBoasVindas ? a label onde ser? colocado uma mensagem de boas vindas.
	 * @param textFieldDataAtual ? p campo onde vai a data atual.
	 * */
	 
	public static void atualizaInterface(JTextField textFieldHoraAtual, JLabel lblBoasVindas, JTextField textFieldDataAtual) {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
	        public void run() {
	        	Calendar now = Calendar.getInstance();
	        	TimeZone timezone = TimeZone.getDefault();
	        	now.setTimeZone(timezone);
	        	
	        	DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	        	
	        	dateFormat.setTimeZone(timezone);
	    		
	        	textFieldHoraAtual.setText(dateFormat.format(now.getTime()));

	        	
	    		int hour = now.get(Calendar.HOUR_OF_DAY);
	    		StringBuilder sb = new StringBuilder(128);
    			
	    		sb.append("<html>");
    			sb.append("<body><p>");
	    		
    			if (hour >= 6 && hour < 12)
	    			sb = sb.append("Bom dia, bem vindo!<br>J? tomou caf??");
	    		else if (hour >= 12 && hour < 15)
	    			sb = sb.append("Boa tarde, bem vindo!<br>J? almo?ou?");
	    		else if (hour >= 15 && hour < 18)
	    			sb = sb.append("Boa tarde, bem vindo!<br>J? fez o lanchinho da tarde?");
	    		else if (hour >= 18 && hour < 22)
	    			sb = sb.append("Boa noite, bem vindo!<br>J? jantou?");
	    		else
	    			sb = sb.append("Boa madrugada, bem vindo!<br>J? assaltou a geladeira?");

	    		sb.append("</p></body>");
    			sb.append("</html>");
	    		
    			lblBoasVindas.setText(sb.toString());
    			
	        	dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	        	dateFormat.setTimeZone(timezone);
	        	textFieldDataAtual.setText(dateFormat.format(now.getTime()));
	        }
	    };
		timer.schedule( task, 0L ,1000L);
	}

	/**
	 * Identifica a funcionalidade e abre uma nova aba com essa funcionalidade e por fim fecha o menu.
	 * @author Eric Carvalho
	 * @param formulario ? o formulario onde se encontra todo o nosso GUI.
	 * @param indexFuncionalidade ? a posi??o da funcionalidade que o usu?rio escolheu.
	 * */
	public static void acessaFuncionalidade(JFrame formulario, int indexFuncionalidade) {
		if (indexFuncionalidade == 0)
			Moeda.main(null);
		else
			Numerico.main(null);
		
		formulario.dispose();
	}
}
