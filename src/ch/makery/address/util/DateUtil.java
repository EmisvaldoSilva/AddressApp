package ch.makery.address.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Funções de ajuda para lidar com datas .
 * 
 * @author Emisvaldo Silva
 */
public class DateUtil {
	
	/** O padrão de data que é utilizada para a conversão . Alterar como você deseja. */
	private static final String DATE_PATTERN = "dd.MM.yyyy";
	
	/** O formatador de data . */
	private static final DateTimeFormatter DATE_FORMATTER = 
			DateTimeFormatter.ofPattern(DATE_PATTERN);
	
	/**
     * Retorna a data dada como uma String bem formatado. O acima definido
     * {@link DateUtil#DATE_PATTERN} is used.
     * 
     * @param date a data a ser devolvido como uma string
     * @return string formatada
     */
    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMATTER.format(date);
    }

    /**
     * Converte uma String no formato do definida {@link DateUtil#DATE_PATTERN} 
     * para uma {@link LocalDate} object.
     * 
     * Retorna NULL se a cadeia não pôde ser convertida.
     * 
     * @param dateString a data as String
     * @return o objeto data ou nulo, se ele não pôde ser convertido
     */
    public static LocalDate parse(String dateString) {
        try {
        	return DATE_FORMATTER.parse(dateString, LocalDate::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Verifica a cadeia se é uma data válida.
     * 
     * @param dateString
     * @return true se a string é uma data válida
     */
    public static boolean validDate(String dateString) {
    	// Tente analisar a cadeia.
    	return DateUtil.parse(dateString) != null;
    }
}
