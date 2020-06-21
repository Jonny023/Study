@Column(name = "VIEW_DATA", nullable = false)
@LOB(type = LobType.CLOB)
public Clob getData() {
       return this.data;
}
