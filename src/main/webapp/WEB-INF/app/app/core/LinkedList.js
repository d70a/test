/**
 * Class implemented linked views for step-by-step moving around registration
 * forms.
 * 
 */
Ext.define('CB.core.LinkedList', {
    extend: 'Ext.app.Controller',
    requires: ['CB.core.Node'],
    tag: 'LinkedList',
    /** Storage for our registration views */
    container: undefined,
    me: this,
    /** Function add view + assotiated server 'allowed id' to list */
    add: function(view, serverId) {
        /** Create new node */
        var node = this.buildNewNode(view, serverId);
        if (!this.container) {
            console.log(this.tag + ': create first node in container');
            this.container = node;
            return;
        }
        /** Find last node in chain and add new to to end of chains */
        var c = this.container;
        while (c.getNext()) {
            c = c.getNext();
        }
        c.setNext(node);
        node.setBack(c);

        console.log(this.tag + ': add node to container');

    },
    /** Function return current (aka active now) view */
    get: function() {
        if (!this.container) {
            console.log(this.tag + ': get command ignored, no nodes in container');
            return undefined;
        }
        var view = this.container.getView();
        console.log(this.tag + ': return current node ['
                + view.displayName + ']');
        return view;
    },
    /** Function return view by 'server id' from linked list. Current active
     * view in list ignored and active view in list not changed. */
    getByServerId: function(serverId) {
        if (!this.container) {
            console.log(this.tag
                    + ': command get by server id ignored, no nodes in container');
            return undefined;
        }
        var view = undefined;

        while (this.container.getView()) {
            if (this.container.getServerId() === serverId) {
                view = this.container.getView();
                break;
            }
            this.container = this.container.getNext();
        }

        console.log(this.tag
                + ': for server id ' + serverId + ' found view '
                + view.displayName);
        return view;
    },
    /** Function move active view to next position in chain. At this stage
     * we check, if next position NOT recommended by server, this mean
     * user stay on not valid view and should be fill this view BEFORE
     * step to next chain. */
    next: function(serverRecommendedStep) {
        if (!this.container) {
            console.log(this.tag + ': next command ignored, no nodes in container');
            return undefined;
        }
        if (!this.isNextAllowedByServer(serverRecommendedStep)) {
            return this.get();
        }
        var next = this.container.getNext();
        if (next) {
            console.log(this.tag + ': move to next node ['
                    + next.getView().displayName + ']');
            this.container = next;
        }
        return this.get();
    },
    /** Function move active view to previous position in chain. */
    back: function() {
        if (!this.container) {
            console.log(this.tag + ': back command ignored, no nodes in container');
            return undefined;
        }
        var back = this.container.getBack();
        if (back) {
            console.log(this.tag + ': move to back node ['
                    + back.getView().displayName + ']');
            this.container = back;
        }
        return this.get();
    },
    /*private*/buildNewNode: function(view, serverId) {
        console.log(this.tag + ': build new node for ['
                + view.displayName + '], server id ' + serverId);
        return new CB.core.Node({view: view, serverId: serverId});
    },
    /*private*/isNextAllowedByServer: function(serverRecommendedStep) {
        if (!serverRecommendedStep) {
            var msg = this.tag + ': next command expect server recommended step';
            console.error(msg);
            throw msg;
        }
        if (!this.container) {
            console.log(this.tag + ': check for allowed next failed, no nodes in container');
            return undefined;
        }

        var c = this.container;
        while (c.getNext()) {
            c = c.getNext();
            console.log(this.tag + ': check for view '
                    + c.getView().displayName + ', server ID '
                    + c.getServerId());

            if (c.getServerId() === serverRecommendedStep) {
                console.log(this.tag + ': next until ' + serverRecommendedStep
                        + 'allowed by view ' + c.getView().displayName);
                return true;
            }
        }
        console.log(this.tag + ': next step not allowed');
        return false;
    }

});


