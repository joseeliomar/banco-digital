package com.example.utils;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.enumeration.Mes;

public class Utils {
	
	/**
	 * Obtém a URI do recurso inserido.
	 * 
	 * @param idRecursoInserido
	 * @return a URI do recurso inserido.
	 */
	public static URI obtemUriRecursoCriado(Object idRecursoInserido) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(idRecursoInserido).toUri();
		return uri;
	}

	/**
	 * Obtem a data formata.
	 * 
	 * @param data
	 * @return a data formata.
	 */
	public static String obtemDataFormatadaSemHorario(LocalDate data) {
		Month mes = data.getMonth();
		int numeroMes = data.getMonthValue();
		String nomeMes = ObtemNomeMes(numeroMes).toLowerCase();
		
		DateTimeFormatter formatter = DateTimeFormatter. ofPattern("dd yyyy");
		String diaAno = data.format(formatter);
		String dataFormatada = null;

		if (mes.equals(Month.MAY)) {
			CharSequence mesCom4letras = nomeMes.subSequence(0, 4);
			dataFormatada = diaAno.replace(" ", " de " + mesCom4letras + " de ");
		} else {
			CharSequence mesCom3letras = nomeMes.subSequence(0, 3);
			dataFormatada = diaAno.replace(" ", " de " + mesCom3letras + ". de ");
		}
		
		return dataFormatada;
	}
	
	/**
	 * Obtem a hora formatada.
	 * 
	 * @param dataHora data com hora.
	 * @return a hora formatada.
	 */
	public static String obtemHoraFormatada(LocalDateTime dataHora) {
		DateTimeFormatter formatter = DateTimeFormatter. ofPattern("HH:mm");
		String horaFormatada = dataHora.format(formatter);
		return horaFormatada;
	}
	
	/**
	 * Obtem o nome do mês.
	 * 
	 * @param numeroMes
	 * @return o nome do mês.
	 */
	private static String ObtemNomeMes(int numeroMes) {
		 Mes mes = Mes.getMes(numeroMes);
		 
		 if (mes != null) {
			 return mes.getNome();
		 }
		 
		return null;
	}

	/**
	 * Retorna true se a primeira opção for igual a alguma das demais opções especificadas,
	 * e retorna false caso o contrário.
	 * 
	 * @param primeiraOpcao
	 * @param demaisOpcoesEspecificadas
	 * @return true se a primeira opção for igual a alguma das demais opções especificadas,
	 * e retorna false caso o contrário.
	 */
	public static boolean primeiraOpcaoIgualAlgumaDemaisOpcoesEspecificadas(Object primeiraOpcao,
			Object... demaisOpcoesEspecificadas) {
		for (Object opcaoEspecificada : demaisOpcoesEspecificadas) {
			if (primeiraOpcao.equals(opcaoEspecificada)) {
				return true;
			}
		}
		return false;
	}
}
