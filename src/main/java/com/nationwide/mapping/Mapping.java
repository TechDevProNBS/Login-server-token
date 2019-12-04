package com.nationwide.mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class Mapping {

	@Autowired
	private ModelMapper modelMapper;

	public <T, S> S map(T source, Class<S> destinationType) {
		return modelMapper.map(source, destinationType);
	}

	public <T, S> List<S> map(List<T> source, Class<S> destinationType) {
		return source.stream().map(sourceItem -> map(sourceItem, destinationType)).collect(Collectors.toList());
	}

	public <T, S> Set<S> map(Set<T> source, Class<S> destinationType) {
		return source.stream().map(sourceItem -> map(sourceItem, destinationType)).collect(Collectors.toSet());
	}
}
