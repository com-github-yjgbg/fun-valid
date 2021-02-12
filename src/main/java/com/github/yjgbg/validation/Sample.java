package com.github.yjgbg.validation;

import com.github.yjgbg.validation.core.BaseValidatorExt;
import com.github.yjgbg.validation.core.Validator;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@ExtensionMethod({BaseValidatorExt.class})
public class Sample {
  public static void main(String[] args) {
    final var entity1 = new Entity1(null, 1L, false, Collections.emptyList());
    final var entity2 = new Entity1("null", 0L, false, Arrays.asList(entity1,entity1,entity1));
    var validator = Validator.<Entity1>none()
            .and(Objects::nonNull, "对象为空:%s".msg())
            .and(Entity1::getField2, field2 -> field2 != null && field2 < 1, "field2应该小于1,但是真实值为%s".msg())
            .and(Entity1::getField1, Objects::nonNull, "field1不得为null".msg())
            .andIter(Entity1::getEntity1List, Validator.<Entity1>none()
                    .and(Entity1::getField1,Objects::nonNull,"对象为空:%s".msg())
                    .and(Entity1::getField2, field2 -> field2 != null && field2 < 1, "field2应该小于1,但是真实值为%s".msg())
            );
    entity2.ap(validator.failFast(true)).wrapper("name").ac(System.out::println);
    System.out.println("------------------------------------------------------");
    entity2.ap(validator.failFast(false)).wrapper("name").ac(System.out::println);
  }
}

@Data
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class Entity1 {
  String field1;
  Long field2;
  Boolean field3;
  List<Entity1> entity1List;
}
