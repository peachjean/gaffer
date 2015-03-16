package io.bunting.gaffer

import javax.inject.Provider
import java.beans.Introspector

/**
 * TODO: Document this class
 */
class BindingDelegate {
  final Object component;
  final Map<String, Provider<?>> context;

  final List fieldsToCascade = [];

  BindingDelegate(Object component, Map<String, Provider<?>> context) {
    this.component = component;
    this.context = context
  }

  String getLabel() { "component" }

  String getLabelFistLetterInUpperCase() { getLabel()[0].toUpperCase() + getLabel().substring(1) }

  void methodMissing(String name, def args) {
    NestingType nestingType = PropertyUtil.nestingType(component, name);
    if (nestingType == NestingType.NA) {
      addError("${getLabelFistLetterInUpperCase()} ${getComponentName()} of type [${component.getClass().canonicalName}] has no appplicable [${name}] property ")
      return;
    }

    String subComponentName
    Class clazz
    Closure closure
    (subComponentName, clazz, closure) = analyzeArgs(args)
    if (clazz != null) {
      Object subComponent = clazz.newInstance()
      if (subComponentName && subComponent.hasProperty(name)) {
        subComponent.name = subComponentName;
      }
      if (closure) {
        BindingDelegate subDelegate = new BindingDelegate(subComponent, context)

        cascadeFields(subDelegate)
        injectParent(subComponent)
        closure.delegate = subDelegate
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()
      }
      PropertyUtil.attach(nestingType, component, subComponent, name)
    } else {
      addError("No 'class' argument specified for [${name}] in ${getLabel()} ${getComponentName()} of type [${component.getClass().canonicalName}]");
    }
  }

  void cascadeFields(BindingDelegate subDelegate) {
    for (String k: fieldsToCascade) {
      subDelegate.metaClass."${k}" = this."${k}"
    }
  }

  void propertyMissing(String name, def value) {
    NestingType nestingType = PropertyUtil.nestingType(component, name);
    if (nestingType == NestingType.NA) {
      addError("${getLabelFistLetterInUpperCase()} ${getComponentName()} of type [${component.getClass().canonicalName}] has no appplicable [${name}] property ")
      return;
    }
    PropertyUtil.attach(nestingType, component, value, name)
  }

  static void addError(String message) {
    throw new GafferException(message);
  }

  def analyzeArgs(Object[] args) {
    String name;
    Class clazz;
    Closure closure;

    if (args.size() > 3) {
      addError("At most 3 arguments allowed but you passed $args")
    }

    if (args[-1] instanceof Closure) {
      closure = args[-1]
      args -= args[-1]
    }

    if (args.size() == 1) {
      clazz = parseClassArgument(args[0])
    }

    if (args.size() == 2) {
      name = parseNameArgument(args[0])
      clazz = parseClassArgument(args[1])
    }

    return [name, clazz, closure]
  }

  Class parseClassArgument(arg) {
    if (arg instanceof Class) {
      return arg
    } else if (arg instanceof String) {
      return Class.forName(arg)
    } else {
      addError("Unexpected argument type ${arg.getClass().canonicalName}")
      return null;
    }
  }

  String parseNameArgument(arg) {
    if (arg instanceof String) {
      return arg
    } else {
      addError("With 2 or 3 arguments, the first argument must be the component name, i.e of type string")
      return null;
    }
  }

  String getComponentName() {
    if (component.hasProperty("name"))
      return "[${component.name}]"
    else
      return ""

  }

  Object ref(String bindingKey)
  {
    return this.context.get(bindingKey).get();
  }

  Object build(Class<?> type, Closure closure)
  {
    return GafferBaseScript.createProvider(this.context, type, closure).get()
  }
  /**
   * TODO: Document this class
   */
  static enum NestingType {
    NA, SINGLE, AS_COLLECTION;
  }

  static class PropertyUtil
  {
    static boolean hasAdderMethod(Object obj, String name) {
      String addMethod = "add${upperCaseFirstLetter(name)}";
      return obj.metaClass.respondsTo(obj, addMethod);
    }

    static NestingType nestingType(Object obj, String name) {
      def decapitalizedName = Introspector.decapitalize(name)
      if (obj.hasProperty(decapitalizedName)) {
        return NestingType.SINGLE;
      }
      if (hasAdderMethod(obj, name)) {
        return NestingType.AS_COLLECTION;
      }
      return NestingType.NA;
    }

    static void attach(NestingType nestingType, Object component, Object subComponent, String name) {
      switch (nestingType) {
        case NestingType.SINGLE:
          name = Introspector.decapitalize(name)
          component."${name}" = subComponent;
          break;
        case NestingType.AS_COLLECTION:
          String firstUpperName = upperCaseFirstLetter(name)
          component."add${firstUpperName}"(subComponent);
          break;
      }
    }

    static String transformFirstLetter(String s, Closure closure) {
      if (s == null || s.length() == 0)
        return s;

      String firstLetter = new String(s.getAt(0));

      String modifiedFistLetter = closure(firstLetter);

      if (s.length() == 1)
        return modifiedFistLetter
      else
        return modifiedFistLetter + s.substring(1);

    }

    static String upperCaseFirstLetter(String s) {
      return transformFirstLetter(s, {String it -> it.toUpperCase()})
    }
  }
}
