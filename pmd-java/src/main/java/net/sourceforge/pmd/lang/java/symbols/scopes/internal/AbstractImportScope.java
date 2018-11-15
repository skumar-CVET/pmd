/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java.symbols.scopes.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import net.sourceforge.pmd.lang.java.symbols.refs.JFieldReference;
import net.sourceforge.pmd.lang.java.symbols.refs.JMethodReference;
import net.sourceforge.pmd.lang.java.symbols.refs.JSymbolicClassReference;
import net.sourceforge.pmd.lang.java.symbols.refs.JVarReference;
import net.sourceforge.pmd.lang.java.symbols.scopes.JScope;


/**
 * Base class for import scopes.
 *
 * <p>Rules for shadowing of imports: bottom of https://docs.oracle.com/javase/specs/jls/se8/html/jls-6.html#jls-6.4.1
 *
 * <p>The simplest way to implement that is to layer the imports into several scopes.
 * See doc on {@link JScope} about the higher level scopes of the scope stacks.
 *
 * @author Clément Fournier
 * @since 7.0.0
 */
abstract class AbstractImportScope extends AbstractExternalScope {

    final Map<String, JSymbolicClassReference> importedTypes = new HashMap<>();
    final Map<String, List<JMethodReference>> importedStaticMethods = new HashMap<>();
    final Map<String, JFieldReference> importedStaticFields = new HashMap<>();


    /**
     * Constructor with the parent scope and the auxclasspath classloader.
     *
     * @param parent      Parent scope
     * @param classLoader ClassLoader used to resolve e.g. import-on-demand
     * @param thisPackage Package name of the current compilation unit, used to check for accessibility
     */
    AbstractImportScope(JScope parent, ClassLoader classLoader, String thisPackage) {
        super(parent, classLoader, thisPackage);
    }


    @Override
    protected Optional<JSymbolicClassReference> resolveTypeNameImpl(String simpleName) {
        return Optional.ofNullable(importedTypes.get(simpleName));
    }


    @Override
    protected Stream<JMethodReference> resolveMethodNameImpl(String simpleName) {
        return importedStaticMethods.getOrDefault(simpleName, Collections.emptyList()).stream();
    }


    @Override
    protected Optional<JVarReference> resolveValueNameImpl(String simpleName) {
        return Optional.ofNullable(importedStaticFields.get(simpleName));
    }
}
