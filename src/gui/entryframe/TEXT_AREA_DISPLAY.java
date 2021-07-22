package gui.entryframe;

public enum TEXT_AREA_DISPLAY {
    ANSWER
    {
        public boolean answer()
        {
            return true;
        }

        public boolean question()
        {
            return false;
        }
    },

    QUESTION()
    {
        public boolean answer()
        {
            return false;
        }

        public boolean question()
        {
            return true;
        }
    };

    public abstract boolean answer();
    public abstract boolean question();
}
