(ns demo.counter
  (:require ["https://cdn.jsdelivr.net/gh/starfederation/datastar@v1.0.4/bundles/datastar-rocket.js" :refer [rocket]]))

;; `^html` emits Rocket's tagged template. Squint's own `#html` returns a
;; string object Rocket will not mount.
(rocket "demo-counter"
        {:mode "light"
         :props (fn [{:keys [number string]}]
                  {:start (-> number (.min 0))
                   :step (-> number (.min 1) (.default 1))
                   :label (-> string .-trim (.default "Count"))})
         :setup (fn [{:strs [$$] :keys [props observeProps cleanup]}]
                  (set! (.-count $$) (.-start props))
                  (cleanup
                   (observeProps
                    (fn [next]
                      (set! (.-count $$) (.-start next)))
                    "start")))
         ;; Reset assigns the signal, with `start` baked into the attribute.
         ;; A start change has to rerender so that value stays current, which
         ;; is the default `renderOnPropChange`.
         :render (fn [{:keys [html] {:keys [label step start]} :props}]
                   #html ^html
                   [:section
                    [:h3 label]
                    [:div
                     [:button {:type "button"
                               :data-on:click (str "$$count -= " step)
                               :data-attr:disabled "$$count <= 0"}
                      "−"]
                     [:output {:data-text "$$count"}]
                     [:button {:type "button"
                               :data-on:click (str "$$count += " step)}
                      "+"]
                     [:template {:data-if (str "$$count !== " start)}
                      [:button {:type "button"
                                :data-on:click (str "$$count = " start)}
                       "Reset"]]]])})
