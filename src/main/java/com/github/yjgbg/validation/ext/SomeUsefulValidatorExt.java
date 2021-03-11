package com.github.yjgbg.validation.ext;

import com.github.yjgbg.validation.core.Getter;
import com.github.yjgbg.validation.core.Validator;
import com.github.yjgbg.validation.core.ValidatorStdExt;
import lombok.experimental.ExtensionMethod;

import java.util.Objects;
import java.util.function.Function;

@ExtensionMethod({ValidatorStdExt.class})
public class SomeUsefulValidatorExt {
  public static <A,B> Validator<A>
  equal(Validator<A> that, Getter<A, B> prop, B value, Function<B,String> message) {
    return that.and(prop,message,x -> Objects.equals(x,value));
  }

  public static <A,B> Validator<A>
  notEquals(Validator<A> that, Getter<A, B> prop, B value, String message) {
    return that.and(prop,message.fmt(),x -> !Objects.equals(x,value));
  }

  public static <A> Validator<A> nonNull(Validator<A> that, String message) {
    return that.and(message.fmt(),Objects::nonNull);
  }

  public static <A,B> Validator<A>
  nonNull(Validator<A> that,Getter<A,B> prop,String message) {
    return that.and(prop,message.fmt(),Objects::nonNull);
  }

  public static <A,B> Validator<A>
  isNull(Validator<A> that,Getter<A,B> prop,String message) {
    return that.and(prop,message.fmt(),Objects::isNull);
  }

  public static <A, B extends Comparable<B>> Validator<A>
  littleThan(Validator<A> that, Getter<A, B> prop, B upperBound, Function<B,String> message) {
    return that.and(prop,message,x -> x!=null && x.compareTo(upperBound) < 0);
  }

  public static <A, B extends Comparable<B>> Validator<A>
  notGreatThan(Validator<A> that, Getter<A, B> prop, B upperBound, Function<B,String> message) {
    return that.and(prop,message,x -> x!=null && x.compareTo(upperBound) <= 0);
  }

  public static <A, B extends Comparable<B>> Validator<A>
  greatThan(Validator<A> that, Getter<A, B> prop, B lowerBound,Function<B,String> message) {
    return that.and(prop,message, x -> x!=null && x.compareTo(lowerBound) > 0);
  }

  public static <A, B extends Comparable<B>> Validator<A>
  notLittleThan(Validator<A> that, Getter<A, B> prop, B lowerBound,Function<B,String> message) {
    return that.and(prop,message, x -> x!=null && x.compareTo(lowerBound) >= 0);
  }

  public static <A, B extends Comparable<B>> Validator<A>
  inRangeInclusive(Validator<A> that, Getter<A, B> prop, B lowerBound, B upperBound, Function<B,String> message) {
    return that.and(prop,message, x -> x!=null && x.compareTo(lowerBound) >= 0 && x.compareTo(upperBound) <= 0);
  }

  public static <A, B extends Comparable<B>> Validator<A>
  inRangeExclusive(Validator<A> that, Getter<A, B> prop, B lowerBound, B upperBound, Function<B,String> message) {
    return that.and(prop,message, x -> x!=null && x.compareTo(lowerBound) > 0 && x.compareTo(upperBound) < 0);
  }
}
