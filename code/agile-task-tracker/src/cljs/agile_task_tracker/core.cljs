(ns agile-task-tracker.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [agile-task-tracker.views.dashboard :refer [dashboard-page]]
              [agile-task-tracker.views.backlog :refer [backlog]]
							[agile-task-tracker.views.project :refer [project-page]]
							[agile-task-tracker.views.sprints :refer [sprints-page]]))

;TODO add history-browser-navigation and # prefix with secretary

;; -------------------------
;; Views
(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes
(secretary/defroute "/" []
                    (session/put! :current-page #'dashboard-page))

(secretary/defroute "/backlog/:project-id" [project-id]
                    (session/put! :project-id project-id)
                    (session/put! :current-page #'backlog))

(secretary/defroute "/project/:organisation-id" [organisation-id]
                    (session/put! :organisation-id organisation-id)
                    (session/put! :current-page #'project-page))

(secretary/defroute "/sprints/:project-id" [project-id]
                    (session/put! :project-id project-id)
                    (session/put! :current-page #'sprints-page))

;; -------------------------
;; Initialize app
(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
