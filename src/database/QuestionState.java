package database;

public enum QuestionState
{
    CORRECT
            {
                @Override
                public boolean correct() {
                    return true;
                }

                @Override
                public boolean inCorrect() {
                    return false;
                }

                @Override
                public boolean noAnswer() {
                    return false;
                }
            },

    IN_CORRECT
            {
                @Override
                public boolean correct() {
                    return false;
                }

                @Override
                public boolean inCorrect() {
                    return true;
                }

                @Override
                public boolean noAnswer() {
                    return false;
                }
            },

    NO_ANSWER
            {
                @Override
                public boolean correct() {
                    return false;
                }

                @Override
                public boolean inCorrect() {
                    return false;
                }

                @Override
                public boolean noAnswer() {
                    return true;
                }
            };

    public abstract boolean correct();
    public abstract boolean inCorrect();
    public abstract boolean noAnswer();
}
