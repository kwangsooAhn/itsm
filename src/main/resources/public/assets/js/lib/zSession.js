const ZSession = {
    data: {},

    add: function (key, value) {
        this.data[key] = value;
    },

    get: function (key) {
        return this.data[key];
    },

    getAll: function () {
        return this.data;
    },

    remove: function (key) {
        delete this.data[key];
    },

    clear: function () {
        this.data = {};
    }
};

export { ZSession };