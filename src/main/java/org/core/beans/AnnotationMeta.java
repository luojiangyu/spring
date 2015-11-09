package org.core.beans;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.core.beans.constant.AnnotationMetaType;

public class AnnotationMeta {
    private AnnotationMetaType AnnotationType;
    private Object target;
    private Set<Annotation> annotation;
    
	public AnnotationMetaType getAnnotationType() {
		return AnnotationType;
	}
	public void setAnnotationType(AnnotationMetaType annotationType) {
		AnnotationType = annotationType;
	}
	public Object getTarget() {
		return target;
	}
	public void setTarget(Object target) {
		this.target = target;
	}
	public Set<Annotation> getAnnotation() {
		return annotation;
	}
	public void setAnnotation(Set<Annotation> annotation) {
		this.annotation = annotation;
	}
    
}
