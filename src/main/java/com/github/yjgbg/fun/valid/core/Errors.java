package com.github.yjgbg.fun.valid.core;

import io.vavr.collection.Map;
import io.vavr.collection.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static io.vavr.API.Map;
import static io.vavr.API.Set;

/**
 * Error的构建，以及相关运算，包括Error的加法，以及Error与string的运算
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Errors {
	private static final Errors NONE = new Errors(null, Set(), Map());
	Object rejectValue;
	Set<String> messages;
	Map<String, Errors> fieldErrors;

	/**
	 * @return 空错误，表示没有错
	 */
	public static Errors none() {
		return NONE;
	}

	/**
	 * 简单错误
	 * 由rejectValue和message组成的简单错误
	 * @param rejectValue 不符合constraint的值
	 * @param message 错误信息
	 * @return 由rejectValue和message组成的简单错误
	 */
	@Contract(pure = true)
	public static Errors simple(@Nullable final Object rejectValue, @NotNull final String message) {
		if (rejectValue == null && message.isBlank()) return none();
		return new Errors(rejectValue, Set(message), Map());
	}

	/**
	 * A类复杂错误
	 * 由两个错误相加而成，对应字段相加
	 * @param errors0 错误0
	 * @param errors1 错误1
	 * @return 由两个错误相加而成，对应字段相加
	 */
	@NotNull
	@Contract(pure = true)
	public static Errors plus(@Nullable final Errors errors0, @Nullable final Errors errors1) {
		if (errors0 == null) return plus(none(), errors1);
		if (errors1 == null) return plus(errors0, none());
		if (errors0 == none()) return errors1;
		if (errors1 == none()) return errors0;
		final var reject0 = errors0.getRejectValue();
		final var reject1 = errors1.getRejectValue();
		if (reject0 != null && reject1 != null && reject0 != reject1)
			throw new IllegalArgumentException(String.format("不同数据对象的校验结果不可相加(%s,%s)", reject0, reject1));
		// 并集
		final var messages =  errors0.getMessages().union(errors1.getMessages());
		final var fieldErrors = errors0.getFieldErrors().merge(errors1.getFieldErrors(), Errors::plus);
		return new Errors(reject0 != null ? reject0 : reject1, messages, fieldErrors);
	}

	/**
	 * B类复杂错误
	 * 根据一个fieldError以及field名，构造一个实体Error
	 * @param key field名
	 * @param errors error
	 * @return errors
	 */
	@Contract(pure = true)
	public static Errors transform(@NotNull final String key, @Nullable final Errors errors) {
		if (errors == null) return none();
		if (errors == none()) return none();
		return new Errors(null, Set(), Map(key, errors));
	}

	/**
	 * 是否包含错误（！是否为空错误）
	 * @return 是否包含错误
	 */
	public boolean hasError() {
		return this != none();
	}
}
