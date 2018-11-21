package pub.betaev.bbb.gradle

trait Ordered<T> {

    private final Set<Class<? extends T>> afters = [];

    void after(Class<? extends T> targetClass) {
        afters.add(targetClass)
    }

    int compare(Ordered<T> that) {
        boolean before = afters.contains(this.class)
        boolean after = afters.contains(that.class)
        if (before && after) {
            throw new IllegalStateException()
        }
        return before ?
                -1 :
                after ?
                        1 :
                        0
    }

}